package com.sample.common;

/**
 * スロットリングする
 */
public class Throttler {

    private long maxRequestsPerPeriod = 0;

    private long timePeriodMillis = 1000;

    private volatile TimeSlot slot = null;

    /**
     * コンストラクタ
     *
     * @param maxRequestsPerPeriod
     */
    public Throttler(int maxRequestsPerPeriod) {
        this.maxRequestsPerPeriod = maxRequestsPerPeriod;
    }

    /**
     * 処理を実行します。
     *
     * @param callback
     */
    public <T> void process(Callback<T> callback) {
        // 秒間アクセス数を超えている場合の遅延させるべきミリ秒数
        long delay = this.calculateDelay();

        if (0 < delay) {
            this.delay(delay);
        }

        callback.execute();
    }

    /**
     * 指定されたミリ秒スリープさせる。
     *
     * @param delay
     * @throws InterruptedException
     */
    protected void delay(long delay) {
        if (delay < 0) {
            return;
        } else {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    protected long calculateDelay() {
        TimeSlot slot = nextSlot();

        if (!slot.isActive()) {
            long delay = slot.startTime - System.currentTimeMillis();
            return delay;
        }

        return 0;
    }

    protected synchronized TimeSlot nextSlot() {
        if (slot == null) {
            slot = new TimeSlot();
        }

        if (slot.isFull()) {
            slot = slot.next();
        }

        slot.assign();

        return slot;
    }

    protected class TimeSlot {

        private volatile long capacity = Throttler.this.maxRequestsPerPeriod;

        private final long duration = Throttler.this.timePeriodMillis;

        private final long startTime;

        protected TimeSlot() {
            this(System.currentTimeMillis());
        }

        protected TimeSlot(long startTime) {
            this.startTime = startTime;
        }

        protected void assign() {
            capacity--;
        }

        protected TimeSlot next() {
            return new TimeSlot(Math.max(System.currentTimeMillis(), this.startTime + this.duration));
        }

        protected boolean isActive() {
            return startTime <= System.currentTimeMillis();
        }

        protected boolean isFull() {
            return capacity <= 0;
        }
    }

    public interface Callback<T> {
        public T execute();
    }
}
