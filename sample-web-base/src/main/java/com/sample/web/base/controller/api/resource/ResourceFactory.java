package com.sample.web.base.controller.api.resource;

/**
 * Resourceファクトリ
 */
public class ResourceFactory {

    /**
     * インスタンスを作成します。
     *
     * @return
     */
    public static Resource create() {
        return new ResourceImpl();
    }
}
