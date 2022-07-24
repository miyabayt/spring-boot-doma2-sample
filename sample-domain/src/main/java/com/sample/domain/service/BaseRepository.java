package com.sample.domain.service;

import com.sample.domain.dto.common.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepository {

  @Autowired protected PageFactory pageFactory;
}
