package com.bigtreetc.sample.web.api.security;

import com.bigtreetc.sample.domain.entity.User;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Getter
public class LoginUser extends org.springframework.security.core.userdetails.User {

  private static final long serialVersionUID = -5891919297179603893L;

  /**
   * コンストラクタ
   *
   * @param user
   * @param authorities
   */
  public LoginUser(User user, Collection<? extends GrantedAuthority> authorities) {
    super(String.valueOf(user.getEmail()), user.getPassword(), authorities);
  }
}
