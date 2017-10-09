package com.sample.domain.service.system;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.HolidayDao;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Holiday;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 祝日サービス
 */
@Service
public class HolidayService extends BaseTransactionalService { // TODO

    @Autowired
    HolidayDao holidayDao;

    /**
     * 祝日を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Holiday> findAll(Holiday where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val holidays = holidayDao.selectAll(where, options, toList());

        return PageFactory.create(holidays, pageable, options.getCount());
    }

    /**
     * 祝日を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Holiday findById(final ID<Holiday> id) {
        // 1件取得
        val holiday = holidayDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));
        return holiday;
    }

    /**
     * 祝日を追加します。
     *
     * @param inputHoliday
     * @return
     */
    public Holiday create(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");

        // 1件登録
        holidayDao.insert(inputHoliday);

        return inputHoliday;
    }

    /**
     * 祝日を更新します。
     *
     * @param inputHoliday
     * @return
     */
    public Holiday update(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");

        // 1件更新
        int updated = holidayDao.update(inputHoliday);

        if (updated < 1) {
            throw new NoDataFoundException("holiday_id=" + inputHoliday.getId() + " のデータが見つかりません。");
        }

        return inputHoliday;
    }

    /**
     * 祝日を論理削除します。
     *
     * @return
     */
    public Holiday delete(final ID<Holiday> id) {
        val holiday = holidayDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("holiday_id=" + id + " のデータが見つかりません。"));

        int updated = holidayDao.delete(holiday);

        if (updated < 1) {
            throw new NoDataFoundException("holiday_id=" + id + " は更新できませんでした。");
        }

        return holiday;
    }
}
