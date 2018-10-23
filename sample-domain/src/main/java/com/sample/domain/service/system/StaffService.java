package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.repository.system.StaffRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 担当者サービス
 */
@Service
public class StaffService extends BaseTransactionalService {

    @Autowired
    StaffRepository staffRepository;

    /**
     * 担当者を複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return staffRepository.findAll(criteria, pageable);
    }

    /**
     * 担当者を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Staff> findOne(StaffCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return staffRepository.findOne(criteria);
    }

    /**
     * 担当者を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Staff findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return staffRepository.findById(id);
    }

    /**
     * 担当者を追加します。
     *
     * @param inputStaff
     * @return
     */
    public Staff create(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");
        return staffRepository.create(inputStaff);
    }

    /**
     * 担当者を更新します。
     *
     * @param inputStaff
     * @return
     */
    public Staff update(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");
        return staffRepository.update(inputStaff);
    }

    /**
     * 担当者を論理削除します。
     *
     * @return
     */
    public Staff delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return staffRepository.delete(id);
    }
}
