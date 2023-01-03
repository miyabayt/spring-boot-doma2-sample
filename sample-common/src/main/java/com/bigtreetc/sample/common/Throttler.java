package com.bigtreetc.sample.common;

import java.util.concurrent.TimeUnit;
import lombok.val;

/** スロットリングする */
public class Throttler {

  private long maxRequestsPerPeriod = 0;

  private long timePeriodMillis = 1000;

  private TimeSlot timeSlot = null;

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
    if (0 < delay) {
      try {
        TimeUnit.MICROSECONDS.sleep(delay);
      } catch (InterruptedException e) {
      }
    }
  }

  protected synchronized long calculateDelay() {
    TimeSlot slot = nextSlot();

    if (!slot.isActive()) {
      return slot.startTime - System.currentTimeMillis();
    }

    return 0;
  }

  protected synchronized TimeSlot nextSlot() {
    if (timeSlot == null) {
      timeSlot = new TimeSlot();
    }

    if (timeSlot.isFull()) {
      timeSlot = timeSlot.next();
    }

    timeSlot.assign();

    return timeSlot;
  }

  protected class TimeSlot {

    private long capacity = Throttler.this.maxRequestsPerPeriod;

    private long duration = Throttler.this.timePeriodMillis;

    private long startTime;

    protected TimeSlot() {
      this(System.currentTimeMillis());
    }

    protected TimeSlot(long startTime) {
      this.startTime = startTime;
    }

    protected synchronized void assign() {
      capacity--;
    }

    protected synchronized TimeSlot next() {
      val startTime = Math.max(System.currentTimeMillis(), this.startTime + this.duration);
      val slot = new TimeSlot(startTime);
      return slot;
    }

    protected synchronized boolean isActive() {
      return startTime <= System.currentTimeMillis();
    }

    protected synchronized boolean isFull() {
      return capacity <= 0;
    }
  }

  public interface Callback<T> {
    T execute();
  }
}
