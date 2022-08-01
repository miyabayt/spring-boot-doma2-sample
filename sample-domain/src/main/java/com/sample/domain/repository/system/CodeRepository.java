package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.CodeDao;
import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.exception.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** コードリポジトリ */
@RequiredArgsConstructor
@Repository
public class CodeRepository {

  @NonNull final CodeDao codeDao;

  /**
   * コードを全件取得します。
   *
   * @return
   */
  public List<Code> fetchAll() {
    val pageable = Pageable.unpaged();
    val options = createSelectOptions(pageable).count();
    return codeDao.selectAll(new CodeCriteria(), options, toList());
  }

  /**
   * コードを複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = codeDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * コードを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Code> findOne(CodeCriteria criteria) {
    return codeDao.select(criteria);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  public Code findById(final Long id) {
    return codeDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コードを追加します。
   *
   * @param inputCode
   * @return
   */
  public Code create(final Code inputCode) {
    codeDao.insert(inputCode);
    return inputCode;
  }

  /**
   * コードを更新します。
   *
   * @param inputCode
   * @return
   */
  public Code update(final Code inputCode) {
    int updated = codeDao.update(inputCode);

    if (updated < 1) {
      throw new NoDataFoundException("code_id=" + inputCode.getId() + " のデータが見つかりません。");
    }

    return inputCode;
  }

  /**
   * コードを論理削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    val code =
        codeDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));

    int updated = codeDao.delete(code);

    if (updated < 1) {
      throw new NoDataFoundException("code_id=" + id + " は更新できませんでした。");
    }

    return code;
  }
}
