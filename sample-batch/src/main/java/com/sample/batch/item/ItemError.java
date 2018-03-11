package com.sample.batch.item;

import lombok.Getter;
import lombok.Setter;

/**
 * 処理対象のエラーメッセージ
 */
@Setter
@Getter
public class ItemError {

    String sourceName;

    int position;

    String errorMessage;
}
