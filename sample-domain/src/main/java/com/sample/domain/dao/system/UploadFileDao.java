package com.sample.domain.dao.system;

import com.sample.domain.dto.system.UploadFile;
import com.sample.domain.dto.system.UploadFileCriteria;
import java.util.List;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface UploadFileDao {

  /**
   * アップロードファイルを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select
  List<UploadFile> selectAll(UploadFileCriteria criteria, SelectOptions options);

  /**
   * アップロードファイルを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  UploadFile selectById(Long id);

  /**
   * アップロードファイルを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  UploadFile select(UploadFileCriteria criteria);

  /**
   * アップロードファイルを登録します。
   *
   * @param uploadFile
   * @return
   */
  @Insert
  int insert(UploadFile uploadFile);

  /**
   * アップロードファイルを更新します。
   *
   * @param uploadFile
   * @return
   */
  @Update
  int update(UploadFile uploadFile);

  /**
   * アップロードファイルを物理削除します。
   *
   * @param uploadFile
   * @return
   */
  @Update(excludeNull = true)
  int delete(UploadFile uploadFile);

  /**
   * アップロードファイルを一括登録します。
   *
   * @param uploadFiles
   * @return
   */
  @BatchInsert
  int[] insert(List<UploadFile> uploadFiles);
}
