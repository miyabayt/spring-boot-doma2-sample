package com.sample.domain.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.domain.dto.common.PageFactory;

public abstract class BaseRepository {

    @Autowired
    protected PageFactory pageFactory;
}
