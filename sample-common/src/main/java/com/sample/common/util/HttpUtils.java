package com.sample.common.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.proxy.ProxyServer;

import lombok.val;

/**
 * HTTP通信ユーティリティ
 */
public class HttpUtils {

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    /**
     * レスポンスを返します。
     *
     * @param rb
     * @return
     */
    public static Response execute(RequestBuilder rb) throws IOException {
        // デフォルトのタイムアウト値を使用する
        return execute(rb, 5000, 10000, 3, null, null);
    }

    /**
     * レスポンスを返します。
     *
     * @param rb
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public static Response execute(RequestBuilder rb, String proxyHost, Integer proxyPort) throws IOException {
        // デフォルトのタイムアウト値を使用する
        return execute(rb, 5000, 10000, 3, proxyHost, proxyPort);
    }

    /**
     * レスポンスを返します。
     *
     * @param rb
     * @param requestTimeoutMillis
     * @param responseTimeoutMillis
     * @param retry
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public static Response execute(RequestBuilder rb, int requestTimeoutMillis, int responseTimeoutMillis, int retry,
            String proxyHost, Integer proxyPort) throws IOException {

        val config = new DefaultAsyncHttpClientConfig.Builder().setRequestTimeout(requestTimeoutMillis)
                .setMaxRequestRetry(retry).build();

        try (val httpClient = new DefaultAsyncHttpClient(config)) {
            // proxyの指定がある場合
            if (!StringUtils.isEmpty(proxyHost)) {
                rb.setProxyServer(new ProxyServer.Builder(proxyHost, proxyPort).build());
            }

            val future = httpClient.executeRequest(rb.build());
            val response = future.get(responseTimeoutMillis, TimeUnit.MILLISECONDS);

            return response;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
