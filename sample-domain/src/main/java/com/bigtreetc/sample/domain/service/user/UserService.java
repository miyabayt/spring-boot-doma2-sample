package com.bigtreetc.sample.domain.service.user;

import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import com.bigtreetc.sample.domain.repository.UserRepository;
import com.bigtreetc.sample.domain.service.BaseTransactionalService;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** ユーザーサービス */
@RequiredArgsConstructor
@Service
public class UserService extends BaseTransactionalService {

  @NonNull final UserRepository userRepository;

  /**
   * ユーザーを複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return userRepository.findAll(criteria, pageable);
  }

  /**
   * ユーザーを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<User> findOne(UserCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return userRepository.findOne(criteria);
  }

  /**
   * ユーザーを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<User> findById(UserCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return userRepository.findOne(criteria);
  }

  /**
   * ユーザーを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public User findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return userRepository.findById(id);
  }

  /**
   * ユーザーを追加します。
   *
   * @param inputUser
   * @return
   */
  public User create(final User inputUser) {
    Assert.notNull(inputUser, "inputUser must not be null");
    return userRepository.create(inputUser);
  }

  /**
   * ユーザーを更新します。
   *
   * @param inputUser
   * @return
   */
  public User update(final User inputUser) {
    Assert.notNull(inputUser, "inputUser must not be null");
    return userRepository.update(inputUser);
  }

  /**
   * ユーザーを論理削除します。
   *
   * @return
   */
  public User delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return userRepository.delete(id);
  }

  /**
   * 顧客マスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, UserCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = userRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, user -> modelMapper.map(user, clazz));
    }
  }
}
