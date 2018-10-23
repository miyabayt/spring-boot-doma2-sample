package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.PermissionDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * 権限リポジトリ
 */
@Repository
public class PermissionRepository extends BaseRepository {

    @Autowired
    PermissionDao permissionDao;

    /**
     * 権限を複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
        // ページングを指定する
        val options = createSelectOptions(pageable).count();
        val data = permissionDao.selectAll(criteria, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * 権限を取得します。
     *
     * @param criteria
     * @return
     */
    public Optional<Permission> findOne(PermissionCriteria criteria) {
        // 1件取得
        return permissionDao.select(criteria);
    }

    /**
     * 権限を取得します。
     *
     * @return
     */
    public Permission findById(final Long id) {
        // 1件取得
        return permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * 権限を追加します。
     *
     * @param inputPermission
     * @return
     */
    public Permission create(final Permission inputPermission) {
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
    public Permission delete(final Long id) {
        val permission = permissionDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));

        int updated = permissionDao.delete(permission);

        if (updated < 1) {
            throw new NoDataFoundException("permission_id=" + id + " は更新できませんでした。");
        }

        return permission;
    }
}
