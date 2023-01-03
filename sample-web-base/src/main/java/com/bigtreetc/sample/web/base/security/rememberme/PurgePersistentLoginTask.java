package com.bigtreetc.sample.web.base.security.rememberme;

import com.bigtreetc.sample.common.util.DateUtils;
import java.time.LocalDateTime;
import lombok.Setter;
import lombok.val;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
public class PurgePersistentLoginTask extends JdbcDaoSupport {

  private static final String removeExpiredTokensSql =
      "DELETE FROM persistent_logins WHERE last_used < ?";

  @Setter private int tokenPurgeSeconds = 30 * 86400;

  @Scheduled(fixedDelay = 86400) // daily
  public void process() {
    val purgeLocalDateTime = LocalDateTime.now().minusSeconds(tokenPurgeSeconds);
    val purgeDate = DateUtils.toDate(purgeLocalDateTime);
    getJdbcTemplate().update(removeExpiredTokensSql, purgeDate);
  }
}
