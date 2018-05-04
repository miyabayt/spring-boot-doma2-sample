package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Staff;

@ConfigAutowireable
@Dao
public interface StaffDao {

    /**
     * 担当者を取得します。
     *
     * @param staff
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final Staff staff, final SelectOptions options, final Collector<Staff, ?, R> collector);

    /**
     * 担当者を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Staff> selectById(Long id);

    /**
     * 担当者を1件取得します。
     *
     * @param staff
     * @return
     */
    @Select
    Optional<Staff> select(Staff staff);

    /**
     * 担当者を登録します。
     *
     * @param Staff
     * @return
     */
    @Insert
    int insert(Staff Staff);

    /**
     * 担当者を更新します。
     *
     * @param staff
     * @return
     */
    @Update
    int update(Staff staff);

    /**
     * 担当者を論理削除します。
     *
     * @param staff
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(Staff staff);

    /**
     * 担当者を一括登録します。
     *
     * @param staffs
     * @return
     */
    @BatchInsert
    int[] insert(List<Staff> staffs);
}
