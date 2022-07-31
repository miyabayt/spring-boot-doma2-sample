package com.sample.domain.repository.users;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.UploadFileDao;
import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dao.users.UserRoleDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.dto.user.UserRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseRepository {

  @Autowired UserDao userDao;

  @Autowired UserRoleDao userRoleDao;

  @Autowired UploadFileDao uploadFileDao;

  /**
   * ユーザーを取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
    // ページングを指定する
    val options = createSelectOptions(pageable).count();
    val data = userDao.selectAll(criteria, options, toList());
    return pageFactory.create(data, pageable, options.getCount());
  }

  /**
   * ユーザーを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<User> findOne(UserCriteria criteria) {
    // 1件取得
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
    return userDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * ユーザーを追加します。
   *
   * @param inputUser
   * @return
   */
  public User create(final User inputUser) {

    // 1件登録
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

    // 1件更新
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
