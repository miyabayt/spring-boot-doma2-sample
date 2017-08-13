package com.sample.web.base.helper;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * デバイス情報ヘルパー
 */
@Component
@Slf4j
public class DeviceHelper {

    private static final LiteDeviceResolver deviceResolver = new LiteDeviceResolver();

    public enum DEVICE {
        FP, PC, SP, Tablet
    }

    private static final List<String> FP_USER_AGENTS = Arrays.asList("DoCoMo", "KDDI", "DDIPOKET", "UP.Browser",
            "J-PHONE", "Vodafone", "SoftBank");

    /**
     * デバイスを判定します。
     * 
     * @param request
     * @return
     */
    public DEVICE determineDevice(HttpServletRequest request) {
        // User-Agentを取り出す
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

        if (StringUtils.isNotEmpty(userAgent)) {
            if (FP_USER_AGENTS.contains(userAgent)) {
                return DEVICE.FP;
            }
        }

        // spring-mobile
        Device device = deviceResolver.resolveDevice(request);

        if (device != null) {
            if (device.isMobile()) {
                return DEVICE.SP;
            } else if (device.isTablet()) {
                return DEVICE.Tablet;
            }
        }

        return DEVICE.PC;
    }
}
