package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dao.system.StaffRoleDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.dto.system.StaffRole;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 担当者リポジトリ */
@Repository
public class StaffRepository extends BaseRepository {

  @Autowired StaffDao staffDao;

  @Autowired StaffRoleDao staffRoleDao;

  /**
   * 担当者を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
    // ページングを指定する
    val options = createSelectOptions(pageable).count();
    val data = staffDao.selectAll(criteria, options, toList());
    return pageFactory.create(data, pageable, options.getCount());
  }

  /**
   * 担当者を取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Staff> findOne(StaffCriteria criteria) {
    return staffDao.select(criteria);
  }

  /**
   * 担当者を取得します。
   *
   * @return
   */
  public Staff findById(final Long id) {
    return staffDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 担当者を追加します。
   *
   * @param inputStaff
   * @return
   */
  public Staff create(final Staff inputStaff) {
    // 1件登録
    staffDao.insert(inputStaff);

    // ロール権限紐付けを登録する
    val staffRole = new StaffRole();
    staffRole.setStaffId(inputStaff.getId());
    staffRole.setRoleCode("admin");
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
  public Staff delete(final Long id) {
    val staff =
        staffDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));

    int updated = staffDao.delete(staff);

    if (updated < 1) {
      throw new NoDataFoundException("staff_id=" + id + " は更新できませんでした。");
    }

    return staff;
  }
}
