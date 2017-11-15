package com.sample.web.base.controller.api.resource;

/**
 * リソースファクトリのデフォルト実装
 */
public class DefaultResourceFactoryImpl implements ResourceFactory {

    /**
     * インスタンスを作成します。
     *
     * @return
     */
    @Override
    public Resource create() {
        return new ResourceImpl();
    }
}
