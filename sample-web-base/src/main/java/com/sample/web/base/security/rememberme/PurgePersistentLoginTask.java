package com.sample.web.base.security.rememberme;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sample.common.util.DateUtils;

import lombok.Setter;
import lombok.val;

@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
public class PurgePersistentLoginTask extends JdbcDaoSupport {

    private static final String removeExpiredTokensSql = "DELETE FROM persistent_logins WHERE last_used < ?";

    @Setter
    private int tokenPurgeSeconds = 30 * 86400;

    @Scheduled(fixedDelay = 86400) // daily
    public void process() {
        val purgeLocalDateTime = LocalDateTime.now().minusSeconds(tokenPurgeSeconds);
        val purgeDate = DateUtils.toDate(purgeLocalDateTime);
        getJdbcTemplate().update(removeExpiredTokensSql, purgeDate);
    }
}
