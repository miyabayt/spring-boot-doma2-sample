package com.sample.batch.jobs.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemWriter;
import com.sample.domain.dao.UserDao;
import com.sample.domain.dto.User;

/**
 * ユーザー
 */
public class UserItemWriter extends BaseItemWriter<User> {

    @Autowired
    UserDao userDao;

    @Override
    protected void doWrite(BatchContext context, List<User> items) {
        userDao.insert(items);
    }
}
