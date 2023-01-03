package com.bigtreetc.sample.batch.jobs.staff;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.jobs.BaseItemWriter;
import com.bigtreetc.sample.domain.dao.StaffDao;
import com.bigtreetc.sample.domain.entity.Staff;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/** 担当者 */
public class StaffItemWriter extends BaseItemWriter<Staff> {

  @Autowired StaffDao staffDao;

  @Override
  protected void doWrite(BatchContext context, List<Staff> items) {
    staffDao.insert(items);
  }
}
