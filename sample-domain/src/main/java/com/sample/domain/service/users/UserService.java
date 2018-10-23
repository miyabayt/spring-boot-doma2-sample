package com.sample.domain.service.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.repository.users.UserRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * ユーザーサービス
 */
@Service
public class UserService extends BaseTransactionalService {

    @Autowired
    UserRepository userRepository;

    /**
     * ユーザーを複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<User> findAll(UserCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findAll(criteria, pageable);
    }

    /**
     * ユーザーを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> findOne(UserCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findOne(criteria);
    }

    /**
     * ユーザーを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(UserCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return userRepository.findOne(criteria);
    }

    /**
     * ユーザーを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return userRepository.findById(id);
    }

    /**
     * ユーザーを追加します。
     *
     * @param inputUser
     * @return
     */
    public User create(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");
        return userRepository.create(inputUser);
    }

    /**
     * ユーザーを更新します。
     *
     * @param inputUser
     * @return
     */
    public User update(final User inputUser) {
        Assert.notNull(inputUser, "inputUser must not be null");
        return userRepository.update(inputUser);
    }

    /**
     * ユーザーを論理削除します。
     *
     * @return
     */
    public User delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return userRepository.delete(id);
    }
}
