package com.sample.domain.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Throwable.class)
public abstract class BaseTransactionalService extends BaseService {

}
