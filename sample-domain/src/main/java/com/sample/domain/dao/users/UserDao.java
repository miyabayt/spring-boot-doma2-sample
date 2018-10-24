package com.sample.domain.dao.users;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;

@ConfigAutowireable
@Dao
public interface UserDao {

    /**
     * ユーザーを取得します。
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final UserCriteria criteria, final SelectOptions options, final Collector<User, ?, R> collector);

    /**
     * ユーザーを1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<User> selectById(Long id);

    /**
     * ユーザーを1件取得します。
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<User> select(UserCriteria criteria);

    /**
     * ユーザーを登録します。
     *
     * @param user
     * @return
     */
    @Insert
    int insert(User user);

    /**
     * ユーザーを更新します。
     *
     * @param user
     * @return
     */
    @Update
    int update(User user);

    /**
     * ユーザーを論理削除します。
     *
     * @param user
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(User user);

    /**
     * ユーザーを一括登録します。
     *
     * @param users
     * @return
     */
    @BatchInsert
    int[] insert(List<User> users);

    /**
     * ユーザーを一括更新します。
     *
     * @param users
     * @return
     */
    @BatchUpdate
    int[] update(List<User> users);
}
