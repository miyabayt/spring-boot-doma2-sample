package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffRole;

@ConfigAutowireable
@Dao
public interface StaffRoleDao {

    /**
     * 担当者権限を取得します。
     *
     * @param staff
     * @param permission
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final Staff staff, final Permission permission, final SelectOptions options,
            final Collector<StaffRole, ?, R> collector);

    /**
     * 担当者権限を取得します。
     * 
     * @param id
     * @param collector
     * @param <R>
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectByStaffId(Long id, final Collector<StaffRole, ?, R> collector);

    /**
     * 担当者権限を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(Long id);

    /**
     * 担当者権限を1件取得します。
     *
     * @param permission
     * @return
     */
    @Select
    Optional<Permission> select(Permission permission);

    /**
     * 担当者権限を登録します。
     *
     * @param staffRole
     * @return
     */
    @Insert(exclude = { "roleName", "permissionKey", "permissionName" })
    int insert(StaffRole staffRole);

    /**
     * 担当者権限を更新します。
     *
     * @param staffRole
     * @return
     */
    @Update(exclude = { "roleName", "permissionKey", "permissionName" })
    int update(StaffRole staffRole);

    /**
     * 担当者権限を論理削除します。
     *
     * @param staffRole
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(StaffRole staffRole);

    /**
     * 担当者権限を一括登録します。
     *
     * @param staffRoles
     * @return
     */
    @BatchInsert(exclude = { "roleName", "permissionKey", "permissionName" })
    int[] insert(List<StaffRole> staffRoles);
}
