package com.sample.domain.dao.system;

import java.util.List;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.RolePermission;

@ConfigAutowireable
@Dao
public interface RolePermissionDao {

    /**
     * 役割権限紐付けを取得します。
     *
     * @param rolePermission
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final RolePermission rolePermission, final SelectOptions options,
            final Collector<RolePermission, ?, R> collector);

    /**
     * 役割権限紐付けを登録します。
     *
     * @param rolePermission
     * @return
     */
    @Insert
    int insert(RolePermission rolePermission);

    /**
     * 役割権限紐付けを更新します。
     *
     * @param rolePermission
     * @return
     */
    @Update
    int update(RolePermission rolePermission);

    /**
     * 役割権限紐付けを一括論理削除します。
     *
     * @param rolePermissions
     * @return
     */
    @BatchUpdate
    int[] delete(List<RolePermission> rolePermissions);

    /**
     * 役割権限紐付けを一括登録します。
     *
     * @param rolePermissions
     * @return
     */
    @BatchInsert
    int[] insert(List<RolePermission> rolePermissions);
}
