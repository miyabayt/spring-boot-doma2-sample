package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.domain.dao.StaffDao;
import com.bigtreetc.sample.domain.dao.StaffRoleDao;
import com.bigtreetc.sample.domain.entity.Staff;
import com.bigtreetc.sample.domain.entity.StaffCriteria;
import com.bigtreetc.sample.domain.entity.StaffRole;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** 担当者マスタリポジトリ */
@RequiredArgsConstructor
@Repository
public class StaffRepository {

  @NonNull final StaffDao staffDao;

  @NonNull final StaffRoleDao staffRoleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  /**
   * 担当者マスタを検索します。
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
   * 担当者マスタを検索します。
   *
   * @return
   */
  public Stream<Staff> findAll(StaffCriteria criteria) {
    return staffDao.selectAll(criteria);
  }

  /**
   * 担当者マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Staff> findOne(StaffCriteria criteria) {
    return staffDao.select(criteria);
  }

  /**
   * 担当者マスタを1件取得します。
   *
   * @return
   */
  public Staff findById(final Long id) {
    return staffDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("staff_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 担当者マスタを登録します。
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
   * 担当者マスタを更新します。
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
   * 担当者マスタを論理削除します。
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
