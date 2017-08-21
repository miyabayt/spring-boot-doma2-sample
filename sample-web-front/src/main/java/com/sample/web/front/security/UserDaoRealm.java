package com.sample.web.front.security;

import org.seasar.doma.jdbc.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sample.domain.dao.UserDao;
import com.sample.domain.dto.User;
import com.sample.web.base.security.BaseRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * フロント側 認証認可
 */
@Component
@Slf4j
public class UserDaoRealm extends BaseRealm {

    @Autowired
    UserDao userDao;

    @Override
    protected UserDetails getLoginUser(String loginId) {

        User user = null;

        try {
            // login_idをメールアドレスと見立てる
            User where = new User();
            where.setEmail(loginId);

            // ユーザーを取得して、セッションに保存する
            user = userDao.select(where)
                    .orElseThrow(() -> new UsernameNotFoundException("no user found. [id=" + loginId + "]"));

        } catch (Exception e) {
            // 0件例外がスローされた場合は何もしない
            // それ以外の例外は、認証エラーの例外で包む
            if (!(e instanceof NoResultException)) {
                throw new UsernameNotFoundException("could not select user.", e);
            }
        }

        return new LoginUser(user);
    }
}
