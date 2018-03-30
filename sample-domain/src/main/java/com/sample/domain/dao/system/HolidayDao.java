package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.Holiday;

@ConfigAutowireable
@Dao
public interface HolidayDao { // TODO

    /**
     * 祝日を取得します。
     *
     * @param holiday
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final Holiday holiday, final SelectOptions options, final Collector<Holiday, ?, R> collector);

    /**
     * 祝日を1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<Holiday> selectById(Integer id);

    /**
     * 祝日を1件取得します。
     *
     * @param holiday
     * @return
     */
    @Select
    Optional<Holiday> select(Holiday holiday);

    /**
     * 祝日を登録します。
     *
     * @param holiday
     * @return
     */
    @Insert
    int insert(Holiday holiday);

    /**
     * 祝日を更新します。
     *
     * @param holiday
     * @return
     */
    @Update
    int update(Holiday holiday);

    /**
     * 祝日を論理削除します。
     *
     * @param holiday
     * @return
     */
    @Update(excludeNull = true) // NULLの項目は更新対象にしない
    int delete(Holiday holiday);

    /**
     * 祝日を一括登録します。
     *
     * @param holidays
     * @return
     */
    @BatchInsert
    int[] insert(List<Holiday> holidays);
}
