package com.sample.domain.service.system;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;
import com.sample.domain.repository.system.RoleRepository;
import com.sample.domain.service.BaseTransactionalService;

/**
 * 役割サービス
 */
@Service
public class RoleService extends BaseTransactionalService {

    @Autowired
    RoleRepository roleRepository;

    /**
     * 役割を複数取得します。
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
        Assert.notNull(criteria, "criteria must not be null");
        return roleRepository.findAll(criteria, pageable);
    }

    /**
     * 役割を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<Role> findOne(RoleCriteria criteria) {
        Assert.notNull(criteria, "criteria must not be null");
        return roleRepository.findOne(criteria);
    }

    /**
     * 役割を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Role findById(final Long id) {
        Assert.notNull(id, "id must not be null");
        return roleRepository.findById(id);
    }

    /**
     * 役割を追加します。
     *
     * @param inputRole
     * @return
     */
    public Role create(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");
        return roleRepository.create(inputRole);
    }

    /**
     * 役割を更新します。
     *
     * @param inputRole
     * @return
     */
    public Role update(final Role inputRole) {
        Assert.notNull(inputRole, "inputRole must not be null");
        return roleRepository.update(inputRole);
    }

    /**
     * 役割を論理削除します。
     *
     * @return
     */
    public Role delete(final Long id) {
        Assert.notNull(id, "id must not be null");
        return roleRepository.delete(id);
    }
}
