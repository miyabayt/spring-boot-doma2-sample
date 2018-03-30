package com.sample.domain.service.system;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dao.system.StaffRoleDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * 担当者サービス
 */
@Service
public class StaffService extends BaseTransactionalService {

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    /**
     * 担当者を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Staff> findAll(Staff where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val staffs = staffDao.selectAll(where, options, toList());

        return pageFactory.create(staffs, pageable, options.getCount());
    }

    /**
     * 担当者を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Staff findById(final Integer id) {
        // 1件取得
        val staff = staffDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));
        return staff;
    }

    /**
     * 担当者を追加します。
     *
     * @param inputStaff
     * @return
     */
    public Staff create(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");

        // 1件登録
        staffDao.insert(inputStaff);

        // 役割権限紐付けを登録する
        val staffRole = new StaffRole();
        staffRole.setStaffId(inputStaff.getId());
        staffRole.setRoleKey("admin");
        staffRoleDao.insert(staffRole);

        return inputStaff;
    }

    /**
     * 担当者を更新します。
     *
     * @param inputStaff
     * @return
     */
    public Staff update(final Staff inputStaff) {
        Assert.notNull(inputStaff, "inputStaff must not be null");

        // 1件更新
        int updated = staffDao.update(inputStaff);

        if (updated < 1) {
            throw new NoDataFoundException("staff_id=" + inputStaff.getId() + " のデータが見つかりません。");
        }

        return inputStaff;
    }

    /**
     * 担当者を論理削除します。
     *
     * @return
     */
    public Staff delete(final Integer id) {
        val staff = staffDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));

        int updated = staffDao.delete(staff);

        if (updated < 1) {
            throw new NoDataFoundException("staff_id=" + id + " は更新できませんでした。");
        }

        return staff;
    }
}
