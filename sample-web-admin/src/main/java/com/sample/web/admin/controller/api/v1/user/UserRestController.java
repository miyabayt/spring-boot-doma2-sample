package com.sample.web.admin.controller.api.v1.user;

import static com.sample.web.base.WebConst.MESSAGE_SUCCESS;

import java.io.IOException;
import java.util.Arrays;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.exception.ValidationErrorException;
import com.sample.domain.service.users.UserService;
import com.sample.web.base.controller.api.AbstractRestController;
import com.sample.web.base.controller.api.resource.PageableResource;
import com.sample.web.base.controller.api.resource.PageableResourceImpl;
import com.sample.web.base.controller.api.resource.Resource;

@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends AbstractRestController {

    @Autowired
    UserService userService;

    @Override
    public String getFunctionName() {
        return "API_USER";
    }

    /**
     * ユーザーを一括取得します。
     *
     * @param query
     * @param page
     * @return
     */
    @GetMapping
    public PageableResource index(UserQuery query, @RequestParam(required = false, defaultValue = "1") int page) throws IOException {
        // 入力値からDTOを作成する
        val where = modelMapper.map(query, User.class);

        // 10件で区切って取得する
        Page<User> users = userService.findAll(where, Pageable.DEFAULT);

        PageableResource resource = modelMapper.map(users, PageableResourceImpl.class);
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * ユーザーを取得します。
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/{userId}")
    public Resource show(@PathVariable Long userId) {
        // 1件取得する
        User user = userService.findById(userId);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * ユーザーを追加します。
     *
     * @param
     */
    @PostMapping
    public Resource create(@Validated @RequestBody User inputUser, Errors errors) {
        // 入力エラーがある場合
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }

        // 1件追加する
        User user = userService.create(inputUser);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * ユーザーを更新します。
     *
     * @param
     */
    @PutMapping(value = "/{userId}")
    public Resource update(@PathVariable("userId") Long userId, @Validated @RequestBody User inputUser, Errors errors) {
        // 入力エラーがある場合
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }

        // 1件更新する
        inputUser.setId(userId);
        User user = userService.update(inputUser);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }

    /**
     * ユーザーを削除します。
     *
     * @param
     */
    @DeleteMapping(value = "/{userId}")
    public Resource delete(@PathVariable("userId") Long userId) {
        // 1件取得する
        User user = userService.delete(userId);

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }
}
