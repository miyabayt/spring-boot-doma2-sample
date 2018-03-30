package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Role;

@ConfigAutowireable
@Dao
public interface RoleDao {

    /**
     * 役割を取得します。
     *
     * @param role
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final Role role, final SelectOptions options, final Collector<Role, ?, R> collector);

    /**
     * 役割を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Role> selectById(Integer id);

    /**
     * 役割を1件取得します。
     *
     * @param role
     * @return
     */
    @Select
    Optional<Role> select(Role role);

    /**
     * 役割を登録します。
     *
     * @param Role
     * @return
     */
    @Insert
    int insert(Role Role);

    /**
     * 役割を更新します。
     *
     * @param role
     * @return
     */
    @Update
    int update(Role role);

    /**
     * 役割を論理削除します。
     *
     * @param role
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(Role role);

    /**
     * 役割を一括登録します。
     *
     * @param roles
     * @return
     */
    @BatchInsert
    int[] insert(List<Role> roles);
}
