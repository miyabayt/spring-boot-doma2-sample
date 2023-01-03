package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.domain.dao.UploadFileDao;
import com.bigtreetc.sample.domain.dao.UserDao;
import com.bigtreetc.sample.domain.dao.UserRoleDao;
import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import com.bigtreetc.sample.domain.entity.UserRole;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** ユーザーリポジトリ */
@RequiredArgsConstructor
@Repository
public class UserRepository {

  @NonNull final UserDao userDao;

  @NonNull final UserRoleDao userRoleDao;

  @NonNull final UploadFileDao uploadFileDao;

  /**
   * ユーザーを取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = userDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * ユーザーを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<User> findOne(UserCriteria criteria) {
    val user = userDao.select(criteria);

    // 添付ファイルを取得する
    user.ifPresent(
        u -> {
          val uploadFileId = u.getUploadFileId();
          val uploadFile = ofNullable(uploadFileId).map(uploadFileDao::selectById);
          uploadFile.ifPresent(u::setUploadFile);
        });

    return user;
  }

  /**
   * ユーザー取得します。
   *
   * @return
   */
  public User findById(final Long id) {
    val user =
        userDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));

    // 添付ファイルを取得する
    val uploadFileId = user.getUploadFileId();
    val uploadFile = ofNullable(uploadFileId).map(uploadFileDao::selectById);
    uploadFile.ifPresent(user::setUploadFile);

    return user;
  }

  /**
   * ユーザーを追加します。
   *
   * @param inputUser
   * @return
   */
  public User create(final User inputUser) {
    userDao.insert(inputUser);

    // ロール権限紐付けを登録する
    val userRole = new UserRole();
    userRole.setUserId(inputUser.getId());
    userRole.setRoleCode("user");
    userRoleDao.insert(userRole);

    return inputUser;
  }

  /**
   * ユーザーを更新します。
   *
   * @param inputUser
   * @return
   */
  public User update(final User inputUser) {
    val uploadFile = inputUser.getUploadFile();
    if (uploadFile != null) {
      // 添付ファイルがある場合は、登録・更新する
      val uploadFileId = inputUser.getUploadFileId();
      if (uploadFileId == null) {
        uploadFileDao.insert(uploadFile);
      } else {
        uploadFileDao.update(uploadFile);
      }

      inputUser.setUploadFileId(uploadFile.getId());
    }

    int updated = userDao.update(inputUser);

    if (updated < 1) {
      throw new NoDataFoundException("user_id=" + inputUser.getId() + " のデータが見つかりません。");
    }

    return inputUser;
  }

  /**
   * ユーザーを論理削除します。
   *
   * @return
   */
  public User delete(final Long id) {
    val user =
        userDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));

    int updated = userDao.delete(user);

    if (updated < 1) {
      throw new NoDataFoundException("user_id=" + id + " は更新できませんでした。");
    }

    return user;
  }
}
