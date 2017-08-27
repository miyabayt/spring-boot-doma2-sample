package com.sample.domain.service.permission;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.PermissionDao;
import com.sample.domain.dto.Permission;
import com.sample.domain.dto.Staff;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 権限サービス
 */
@Service
public class PermissionService extends BaseTransactionalService {

    @Autowired
    PermissionDao permissionDao;

    /**
     * 権限を一括取得します。
     * 
     * @param staff
     * @param where
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Permission> findAll(Staff staff, Permission where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val permissions = permissionDao.selectAll(staff, where, options, toList());

        return PageFactory.create(permissions, pageable, options.getCount());
    }

    /**
     * 権限を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Permission findById(final ID<Permission> id) {
        // 1件取得
        val permission = permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));
        return permission;
    }

    /**
     * 権限を追加します。
     *
     * @param inputPermission
     * @return
     */
    public Permission create(final Permission inputPermission) {
        Assert.notNull(inputPermission, "inputPermission must not be null");

        // 1件登録
        permissionDao.insert(inputPermission);

        return inputPermission;
    }

    /**
     * 権限を更新します。
     *
     * @param inputPermission
     * @return
     */
    public Permission update(final Permission inputPermission) {
        Assert.notNull(inputPermission, "inputPermission must not be null");

        // 1件更新
        int updated = permissionDao.update(inputPermission);

        if (updated < 1) {
            throw new NoDataFoundException("permission_id=" + inputPermission.getId() + " のデータが見つかりません。");
        }

        return inputPermission;
    }

    /**
     * 権限を論理削除します。
     *
     * @return
     */
    public Permission delete(final ID<Permission> id) {
        val permission = permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));

        int updated = permissionDao.delete(permission);

        if (updated < 1) {
            throw new NoDataFoundException("permission_id=" + id + " は更新できませんでした。");
        }

        return permission;
    }
}
