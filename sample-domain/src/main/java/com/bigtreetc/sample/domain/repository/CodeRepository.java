package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.domain.dao.CodeDao;
import com.bigtreetc.sample.domain.entity.Code;
import com.bigtreetc.sample.domain.entity.CodeCriteria;
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

/** コードマスタリポジトリ */
@RequiredArgsConstructor
@Repository
public class CodeRepository {

  @NonNull final CodeDao codeDao;

  /**
   * コードマスタを検索します。
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
   * コードマスタを検索します。
   *
   * @return
   */
  public Stream<Code> findAll(CodeCriteria criteria) {
    return codeDao.selectAll(criteria);
  }

  /**
   * コードマスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Code> findOne(CodeCriteria criteria) {
    return codeDao.select(criteria);
  }

  /**
   * コードマスタを1件取得します。
   *
   * @return
   */
  public Code findById(final Long id) {
    return codeDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コードマスタを登録します。
   *
   * @param inputCode
   * @return
   */
  public Code create(final Code inputCode) {
    codeDao.insert(inputCode);
    return inputCode;
  }

  /**
   * コードマスタを更新します。
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
   * コードマスタを論理削除します。
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
