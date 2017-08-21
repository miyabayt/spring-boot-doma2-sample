package com.sample.web.base.security.rememberme;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import com.sample.common.util.DateUtils;
import com.sample.web.base.util.RequestUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiDeviceRememberMeServices extends AbstractRememberMeServices {

    public static final int DEFAULT_SERIES_LENGTH = 16;
    public static final int DEFAULT_TOKEN_LENGTH = 16;

    private MultiDeviceTokenRepository tokenRepository;

    private SecureRandom random;

    /**
     * コンストラクタ
     * 
     * @param key
     * @param userDetailsService
     * @param tokenRepository
     */
    public MultiDeviceRememberMeServices(String key, UserDetailsService userDetailsService,
            MultiDeviceTokenRepository tokenRepository) {
        super(key, userDetailsService);

        this.random = new SecureRandom();
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
            HttpServletResponse response) {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                    + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        MultiDeviceRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        val username = token.getUsername();
        val series = token.getSeries();
        val tokenValue = token.getTokenValue();
        val ipAddress = token.getRemoteAddress();
        val userAgent = token.getUserAgent();

        // We have a match for this user/series combination
        if (!presentedToken.equals(tokenValue)) {
            // Token doesn't match series value. Delete all logins for this user and throw
            // an exception to warn them.
            tokenRepository.removeAllUserTokens(username);

            val message = messages.getMessage("PersistentTokenBasedRememberMeServices.cookieStolen",
                    "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
            throw new CookieTheftException(message);
        }

        val lastUsed = DateUtils.toDate(token.getLastUsed());
        if (lastUsed.getTime() + getTokenValiditySeconds() * 1000L < System.currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }

        // Token also matches, so login is valid. Update the token value, keeping the
        // *same* series number.
        if (log.isDebugEnabled()) {
            log.debug("Refreshing persistent login token for user '{}', series '{}'",
                    new String[] { username, series });
        }

        val newTokenValue = generateTokenData();
        val newToken = new MultiDeviceRememberMeToken();
        val newLastUsed = LocalDateTime.now();
        newToken.setUsername(username);
        newToken.setSeries(series);
        newToken.setRemoteAddress(ipAddress);
        newToken.setUserAgent(userAgent);
        newToken.setTokenValue(newTokenValue);
        newToken.setLastUsed(newLastUsed);

        try {
            tokenRepository.updateToken(series, newTokenValue, DateUtils.toDate(newLastUsed));
            addCookie(newToken, request, response);
        } catch (Exception e) {
            log.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem");
        }

        return getUserDetailsService().loadUserByUsername(username);
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication successfulAuthentication) {

        val username = successfulAuthentication.getName();
        if (log.isDebugEnabled()) {
            log.debug("Creating new persistent login for user [{}]", username);
        }

        val series = generateSeriesData();
        val tokenValue = generateTokenData();
        val lastUsed = LocalDateTime.now();
        val ipAddress = request.getRemoteAddr();
        val userAgent = getUserAgent(request);

        val token = new MultiDeviceRememberMeToken();
        token.setUsername(username);
        token.setSeries(series);
        token.setRemoteAddress(ipAddress);
        token.setUserAgent(userAgent);
        token.setTokenValue(tokenValue);
        token.setLastUsed(lastUsed);

        try {
            tokenRepository.createNewToken(token);
            addCookie(token, request, response);
        } catch (Exception e) {
            log.error("Failed to save persistent token ", e);
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);

        if (authentication != null) {
            val username = authentication.getName();
            val ipAddress = request.getRemoteAddr();
            val userAgent = getUserAgent(request);
            tokenRepository.removeUserTokens(username, ipAddress, userAgent);
        }
    }

    protected String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    protected String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

    protected String getUserAgent(HttpServletRequest request) {
        String trimmed = null;
        String userAgent = RequestUtils.getUserAgent(request);
        if (StringUtils.isNotEmpty(userAgent)) {
            trimmed = userAgent.substring(0, Math.min(userAgent.length(), 200));
        }
        return trimmed;
    }

    protected void addCookie(MultiDeviceRememberMeToken token, HttpServletRequest request,
            HttpServletResponse response) {
        val series = token.getSeries();
        val tokenValue = token.getTokenValue();
        setCookie(new String[] { series, tokenValue }, getTokenValiditySeconds(), request, response);
    }
}
