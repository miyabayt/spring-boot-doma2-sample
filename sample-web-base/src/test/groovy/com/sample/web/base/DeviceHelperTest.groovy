package com.sample.web.base

import com.sample.web.base.helper.DeviceHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import spock.lang.Specification

import static com.sample.web.base.helper.DeviceHelper.DEVICE
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest(classes = DeviceHelper, webEnvironment = WebEnvironment.NONE)
class DeviceHelperTest extends Specification {

    @Autowired
    DeviceHelper deviceHelper

    /**
     * PCと判別されること
     *
     * @throws Exception
     */
    def "UserAgentが未設定の場合はPCと判別されること"() {
        given:
        def request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        expect:
        DEVICE device = deviceHelper.determineDevice(request)
        device == DEVICE.PC
    }

    /**
     * FPと判別されること
     *
     * @throws Exception
     */
    def "UserAgentがKDDIの場合はガラケーと判別されること"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        request.addHeader("User-Agent", "KDDI")
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        expect:
        DEVICE device = deviceHelper.determineDevice(request)
        device == DEVICE.FP
    }
}
