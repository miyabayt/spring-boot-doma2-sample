package com.sample.domain.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Holiday;
import com.sample.domain.repository.system.HolidayRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 祝日サービス
 */
@Service
public class HolidayService extends BaseTransactionalService {

    @Autowired
    HolidayRepository holidayRepository;

    /**
     * 祝日を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Holiday> findAll(Holiday where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");
        return holidayRepository.findAll(where, pageable);
    }

    /**
     * 祝日を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Holiday findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return holidayRepository.findById(id);
    }

    /**
     * 祝日を追加します。
     *
     * @param inputHoliday
     * @return
     */
    public Holiday create(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");
        return holidayRepository.create(inputHoliday);
    }

    /**
     * 祝日を更新します。
     *
     * @param inputHoliday
     * @return
     */
    public Holiday update(final Holiday inputHoliday) {
        Assert.notNull(inputHoliday, "inputHoliday must not be null");
        return holidayRepository.update(inputHoliday);
    }

    /**
     * 祝日を論理削除します。
     *
     * @return
     */
    public Holiday delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return holidayRepository.delete(id);
    }
}
