package com.sample.domain.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.Permission;
import com.sample.domain.dto.Staff;
import com.sample.domain.dto.StaffRole;
import com.sample.domain.dto.common.ID;

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
    <R> R selectByStaffId(ID<Staff> id, final Collector<StaffRole, ?, R> collector);

    /**
     * 担当者権限を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(ID<Permission> id);

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
     * @param Permission
     * @return
     */
    @Insert
    int insert(StaffRole Permission);

    /**
     * 担当者権限を更新します。
     *
     * @param permission
     * @return
     */
    @Update
    int update(StaffRole permission);

    /**
     * 担当者権限を論理削除します。
     *
     * @param permission
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(StaffRole permission);

    /**
     * 担当者権限を一括登録します。
     *
     * @param permissions
     * @return
     */
    @BatchInsert
    int[] insert(List<StaffRole> permissions);
}
