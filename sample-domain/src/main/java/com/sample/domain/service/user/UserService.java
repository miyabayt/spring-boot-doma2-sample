package com.sample.domain.service.user;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.UploadFileDao;
import com.sample.domain.dao.user.UserDao;
import com.sample.domain.dao.user.UserRoleDao;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * ユーザーサービス
 */
@Service
public class UserService extends BaseTransactionalService {

    @Autowired
    UserDao userDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    UploadFileDao uploadFileDao;

    /**
     * ユーザーを一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<User> findAll(User where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val users = userDao.selectAll(where, options, toList());

        return PageFactory.create(users, pageable, options.getCount());
    }

    /**
     * ユーザーを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(final ID<User> id) {
        // 1件取得
        val user = userDao.selectById(id)
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
        Assert.notNull(inputUser, "inputUser must not be null");

        // 1件登録
        userDao.insert(inputUser);

        // 役割権限紐付けを登録する
        val userRole = new UserRole();
        userRole.setUserId(inputUser.getId());
        userRole.setRoleKey("user");
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
        Assert.notNull(inputUser, "inputUser must not be null");

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
    public User delete(final ID<User> id) {
        val user = userDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("user_id=" + id + " のデータが見つかりません。"));

        int updated = userDao.delete(user);

        if (updated < 1) {
            throw new NoDataFoundException("user_id=" + id + " は更新できませんでした。");
        }

        return user;
    }
}
