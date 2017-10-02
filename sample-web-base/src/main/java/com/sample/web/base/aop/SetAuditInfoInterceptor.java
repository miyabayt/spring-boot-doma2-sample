package com.sample.web.base.aop;

import static java.util.Optional.ofNullable;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * ログインユーザーを監査情報ホルダーに設定する
 */
@Slf4j
public class SetAuditInfoInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        val now = LocalDateTime.now();

        // 未ログインの場合は、ゲスト扱いにする
        AuditInfoHolder.set("GUEST", now);

        // ログインユーザーが存在する場合
        getLoginUser().ifPresent(loginUser -> {
            // 監査情報を設定する
            AuditInfoHolder.set(loginUser.getUsername(), now);
        });

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後

        // 監査情報をクリアする
        AuditInfoHolder.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
    }

    /**
     * ログインユーザを取得する
     *
     * @return
     */
    protected Optional<UserDetails> getLoginUser() {
        val auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Object principal = auth.getPrincipal();

            if (principal instanceof UserDetails) {
                return ofNullable((UserDetails) principal);
            }
        }

        return Optional.empty();
    }
}
