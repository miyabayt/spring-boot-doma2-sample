package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Permission;

@ConfigAutowireable
@Dao
public interface PermissionDao {

    /**
     * 権限を取得します。
     *
     * @param permission
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final Permission permission, final SelectOptions options,
            final Collector<Permission, ?, R> collector);

    /**
     * 権限を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(Integer id);

    /**
     * 権限を1件取得します。
     *
     * @param permission
     * @return
     */
    @Select
    Optional<Permission> select(Permission permission);

    /**
     * 権限を登録します。
     *
     * @param Permission
     * @return
     */
    @Insert
    int insert(Permission Permission);

    /**
     * 権限を更新します。
     *
     * @param permission
     * @return
     */
    @Update
    int update(Permission permission);

    /**
     * 権限を論理削除します。
     *
     * @param permission
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(Permission permission);

    /**
     * 権限を一括登録します。
     *
     * @param permissions
     * @return
     */
    @BatchInsert
    int[] insert(List<Permission> permissions);
}
