package com.bigtreetc.sample.batch.jobs.birthdayMail;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.batch.jobs.BasePageableItemReader;
import com.bigtreetc.sample.domain.dao.UserDao;
import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

/** 誕生メールの対象を検索する */
public class BirthdayMailUserItemReader extends BasePageableItemReader<User> {

  @Autowired UserDao userDao;

  @Override
  protected List<User> getList() {
    val criteria = new UserCriteria();
    val options = getSelectOptions();
    return userDao.selectAll(criteria, options, toList());
  }
}
