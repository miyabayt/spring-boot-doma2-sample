package com.bigtreetc.sample.domain.service.staff;

import com.bigtreetc.sample.domain.entity.Staff;
import com.bigtreetc.sample.domain.entity.StaffCriteria;
import com.bigtreetc.sample.domain.repository.StaffRepository;
import com.bigtreetc.sample.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.domain.util.CsvUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** 担当者マスタサービス */
@RequiredArgsConstructor
@Service
public class StaffService extends BaseTransactionalService {

  @NonNull final StaffRepository staffRepository;

  /**
   * 担当者マスタを検索します。
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
   * 担当者マスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Staff> findOne(StaffCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return staffRepository.findOne(criteria);
  }

  /**
   * 担当者マスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Staff findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return staffRepository.findById(id);
  }

  /**
   * 担当者マスタを追加します。
   *
   * @param inputStaff
   * @return
   */
  public Staff create(final Staff inputStaff) {
    Assert.notNull(inputStaff, "inputStaff must not be null");
    return staffRepository.create(inputStaff);
  }

  /**
   * 担当者マスタを更新します。
   *
   * @param inputStaff
   * @return
   */
  public Staff update(final Staff inputStaff) {
    Assert.notNull(inputStaff, "inputStaff must not be null");
    return staffRepository.update(inputStaff);
  }

  /**
   * 担当者マスタを論理削除します。
   *
   * @return
   */
  public Staff delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return staffRepository.delete(id);
  }

  /**
   * 担当者マスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, StaffCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = staffRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, staff -> modelMapper.map(staff, clazz));
    }
  }
}
