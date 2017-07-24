package com.sample.batch.jobs.user;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.sample.domain.dao.UserDao;
import com.sample.domain.dto.User;

public class UserItemWriter implements ItemWriter<User> {

    @Autowired
    UserDao userDao;

    @SuppressWarnings("unchecked")
    @Override
    public void write(List<? extends User> items) throws Exception {
        userDao.insert((List<User>) items);
    }
}
