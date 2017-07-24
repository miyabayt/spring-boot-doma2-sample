package com.sample.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import com.sample.domain.dto.Code;

@ConfigAutowireable
@Dao
public interface CodeDao {

    /**
     * コード定義を取得します。
     *
     * @return
     */
    @Select
    List<Code> selectAll();
}
