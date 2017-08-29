package com.sample.domain.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.Permission;
import com.sample.domain.dto.User;
import com.sample.domain.dto.UserRole;
import com.sample.domain.dto.common.ID;

@ConfigAutowireable
@Dao
public interface UserRoleDao {

    /**
     * 権限を取得します。
     *
     * @param user
     * @param permission
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final User user, final Permission permission, final SelectOptions options,
            final Collector<UserRole, ?, R> collector);

    /**
     * 権限を取得します。
     * 
     * @param id
     * @param collector
     * @param <R>
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectByUserId(ID<User> id, final Collector<UserRole, ?, R> collector);

    /**
     * 権限を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Permission> selectById(ID<Permission> id);

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
     * @param userRole
     * @return
     */
    @Insert
    int insert(UserRole userRole);

    /**
     * 権限を更新します。
     *
     * @param userRole
     * @return
     */
    @Update
    int update(UserRole userRole);

    /**
     * 権限を論理削除します。
     *
     * @param userRole
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(UserRole userRole);

    /**
     * 権限を一括登録します。
     *
     * @param userRoles
     * @return
     */
    @BatchInsert
    int[] insert(List<UserRole> userRoles);
}
