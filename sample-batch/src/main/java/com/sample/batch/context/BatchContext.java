package com.sample.batch.context;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * バッチ処理コンテキスト
 */
@Setter
@Getter
@NoArgsConstructor
public class BatchContext {

    String batchId;

    String batchName;

    LocalDateTime startDateTime;

    private final AtomicLong processCount = new AtomicLong(0);

    private final AtomicLong errorCount = new AtomicLong(0);

    private final AtomicLong totalCount = new AtomicLong(0);

    // 追加情報（任意のオブジェクトを伝搬させたい場合のため）
    private final Map<String, Object> additionalInfo = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    /**
     * バッチIDとバッチ名を設定します。
     *
     * @param batchId
     * @param batchName
     * @param localDateTime
     */
    public void set(String batchId, String batchName, LocalDateTime localDateTime) {
        synchronized (lock) {
            this.batchId = batchId;
            this.batchName = batchName;
            this.startDateTime = localDateTime;
        }
    }

    /**
     * 処理件数を加算します。
     */
    public void increaseProcessCount() {
        processCount.incrementAndGet();
    }

    /**
     * 処理件数を返します。
     * 
     * @return
     */
    public long getProcessCount() {
        return processCount.intValue();
    }

    /**
     * エラー件数を加算します。
     */
    public void increaseErrorCount() {
        errorCount.incrementAndGet();
    }

    /**
     * エラー件数を返します。
     * 
     * @return
     */
    public long getErrorCount() {
        return errorCount.intValue();
    }

    /**
     * 対象件数を加算します。
     */
    public void increaseTotalCount() {
        totalCount.incrementAndGet();
    }

    /**
     * 対象件数を返します。
     * 
     * @return
     */
    public long getTotalCount() {
        return totalCount.intValue();
    }

    /**
     * 保持している情報をクリアします。
     */
    public void clear() {
        synchronized (lock) {
            batchId = null;
            batchName = null;
            startDateTime = null;
            processCount.set(0);
            errorCount.set(0);
            totalCount.set(0);
            additionalInfo.clear();
        }
    }
}
