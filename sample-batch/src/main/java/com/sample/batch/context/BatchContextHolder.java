package com.sample.batch.context;

public class BatchContextHolder {

    private static final BatchContext JOB_CONTEXT = new BatchContext();

    /**
     * コンテキストを返します。
     *
     * @return
     */
    public static BatchContext getContext() {
        return JOB_CONTEXT;
    }

    /**
     * バッチIDとバッチ名を設定します。
     * 
     * @param batchId
     * @param batchName
     */
    public static void setIdAndName(String batchId, String batchName) {
        JOB_CONTEXT.setBatchId(batchId);
        JOB_CONTEXT.setBatchName(batchName);
    }

    /**
     * コンテキストをクリアします。
     */
    public static void clear() {
        JOB_CONTEXT.clear();
    }
}
