package com.bigtreetc.sample.domain;

import com.bigtreetc.sample.domain.entity.common.DomaEntity;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.PropertyInfo;

/** ModelMapperのファクトリ */
public class DefaultModelMapperFactory {

  /**
   * ModelMapperを返します。
   *
   * @return
   */
  public static ModelMapper create() {
    // ObjectMappingのためのマッパー
    val modelMapper = new ModelMapper();
    val configuration = modelMapper.getConfiguration();

    configuration.setPropertyCondition(
        // IDフィールド以外をマッピングする
        context -> {
          // DomaDtoのIDカラムは上書きしないようにする
          PropertyInfo propertyInfo = context.getMapping().getLastDestinationProperty();
          return !(context.getParent().getDestination() instanceof DomaEntity
              && propertyInfo.getName().equals("id"));
        });

    // 厳格にマッピングする
    configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    configuration.setFullTypeMatchingRequired(true);

    return modelMapper;
  }
}
