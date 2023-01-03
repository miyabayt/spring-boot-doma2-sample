package com.bigtreetc.sample.batch.item;

/** 走査位置 */
public interface ItemPosition {

  String getSourceName();

  void setSourceName(String sourceName);

  int getPosition();

  void setPosition(int position);

  default boolean isFirst() {
    return getPosition() == 1;
  }
}
