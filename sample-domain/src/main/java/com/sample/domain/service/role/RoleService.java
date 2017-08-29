package com.sample.domain.service.role;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.RoleDao;
import com.sample.domain.dao.RolePermissionDao;
import com.sample.domain.dto.Role;
import com.sample.domain.dto.RolePermission;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 役割サービス
 */
@Service
public class RoleService extends BaseTransactionalService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    RolePermissionDao rolePermissionDao;

    /**
     * 役割を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Role> findAll(Role where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val roles = roleDao.selectAll(where, options, toList());

        return PageFactory.create(roles, pageable, options.getCount());
    }

    /**
     * 役割を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Role findById(final ID<Role> id) {
        // 1件取得
        val role = roleDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

        List<RolePermission> rolePermissions = findRolePermissions(role);
        if (CollectionUtils.isNotEmpty(rolePermissions)) {
            Map<Integer, Boolean> permissions = new HashMap<>();
            rolePermissions.stream().forEach(rp -> permissions.putIfAbsent(rp.getPermissionId(), true));
            role.setPermissions(permissions);
        }

        return role;
    }

    /**
     * 役割を追加します。
     *
     * @param inputRole
     * @return
     */
    public Role create(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");

        // 1件登録
        roleDao.insert(inputRole);

        // 役割権限紐付けを登録する
        insertRolePermissions(inputRole);

        return inputRole;
    }

    /**
     * 役割を更新します。
     *
     * @param inputRole
     * @return
     */
    public Role update(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");

        // 1件更新
        int updated = roleDao.update(inputRole);

        if (updated < 1) {
            throw new NoDataFoundException("role_id=" + inputRole.getId() + " のデータが見つかりません。");
        }

        // 役割権限紐付けを論理削除する
        deleteRolePermissions(inputRole);

        // 役割権限紐付けを登録する
        insertRolePermissions(inputRole);

        return inputRole;
    }

    /**
     * 役割を論理削除します。
     *
     * @return
     */
    public Role delete(final ID<Role> id) {
        val role = roleDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

        int updated = roleDao.delete(role);

        if (updated < 1) {
            throw new NoDataFoundException("role_id=" + id + " は更新できませんでした。");
        }

        // 役割権限紐付けを論理削除する
        deleteRolePermissions(role);

        return role;
    }

    /**
     * 役割権限紐付けを登録する
     *
     * @param inputRole
     */
    protected void insertRolePermissions(final Role inputRole) {
        // 入力値がない場合はスキップする
        if (MapUtils.isNotEmpty(inputRole.getPermissions())) {
            List<RolePermission> rolePermissionsToInsert = new ArrayList<>();

            // 権限のチェックがある場合
            inputRole.getPermissions().forEach((key, value) -> {
                // チェックされている
                if (BooleanUtils.isTrue(value)) {
                    val rolePermission = new RolePermission();
                    rolePermission.setRoleKey(inputRole.getRoleKey());
                    rolePermission.setPermissionId(key);
                    rolePermissionsToInsert.add(rolePermission);
                }
            });

            // 一括登録
            rolePermissionDao.insert(rolePermissionsToInsert);
        }
    }

    /**
     * 役割権限紐付けを論理削除する
     * 
     * @param inputRole
     */
    protected void deleteRolePermissions(final Role inputRole) {
        List<RolePermission> rolePermissionsToDelete = findRolePermissions(inputRole);

        if (CollectionUtils.isNotEmpty(rolePermissionsToDelete)) {
            rolePermissionDao.delete(rolePermissionsToDelete);// 一括論理削除
        }
    }

    /**
     * 役割権限紐付けを取得する
     * 
     * @param inputRole
     * @return
     */
    protected List<RolePermission> findRolePermissions(Role inputRole) {
        // 役割権限紐付けを役割キーで取得する
        val where = new RolePermission();
        where.setRoleKey(inputRole.getRoleKey());

        val options = createSearchOptions(Pageable.NO_LIMIT_PAGEABLE);
        return rolePermissionDao.selectAll(where, options, toList());
    }
}
