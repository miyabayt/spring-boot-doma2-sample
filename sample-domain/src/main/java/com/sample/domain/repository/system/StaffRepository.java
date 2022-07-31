package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.RolePermissionDao;
import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dao.system.StaffRoleDao;
import com.sample.domain.dto.system.Staff;
import com.sample.domain.dto.system.StaffCriteria;
import com.sample.domain.dto.system.StaffRole;
import com.sample.domain.exception.NoDataFoundException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** 担当者リポジトリ */
@RequiredArgsConstructor
@Repository
public class StaffRepository {

  @NonNull final StaffDao staffDao;

  @NonNull final StaffRoleDao staffRoleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  /**
   * 担当者を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Staff> findAll(StaffCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = staffDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
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
