package com.sample.batch.jobs.staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemWriter;
import com.sample.domain.dao.system.StaffDao;
import com.sample.domain.dto.system.Staff;

/**
 * 担当者
 */
public class StaffItemWriter extends BaseItemWriter<Staff> {

    @Autowired
    StaffDao staffDao;

    @Override
    protected void doWrite(BatchContext context, List<Staff> items) {
        staffDao.insert(items);
    }
}
