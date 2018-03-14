package com.sample.web.admin.controller.api.v1.user;

import static com.sample.web.base.WebConst.MESSAGE_SUCCESS;

import java.util.Arrays;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.user.User;
import com.sample.domain.service.user.UserService;
import com.sample.web.base.controller.api.AbstractRestController;
import com.sample.web.base.controller.api.resource.PageableResource;
import com.sample.web.base.controller.api.resource.PageableResourceImpl;
import com.sample.web.base.controller.api.resource.Resource;
import com.sample.domain.exception.ValidationErrorException;

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
     * @param page
     * @return
     */
    @ApiOperation(value = "ユーザ情報一括取得", notes = "ユーザーを一括取得します。", response = PageableResource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "ユーザ情報リスト", response = PageableResource.class)})
    @GetMapping
    public PageableResource index(@RequestParam(required = false, defaultValue = "1") int page) {
        // 10件で区切って取得する
        Page<User> users = userService.findAll(new User(), Pageable.DEFAULT_PAGEABLE);

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
    @ApiOperation(value = "ユーザ情報取得", notes = "ユーザーを取得します。", httpMethod = "GET", consumes = "application/json", response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "指定されたユーザ情報", response = Resource.class)})
    @GetMapping(value = "/{userId}")
    public Resource show(@PathVariable Integer userId) {
        // 1件取得する
        User user = userService.findById(ID.of(userId));

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
    public Resource create(@Validated User inputUser, Errors errors) {
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
    public Resource update(@PathVariable("userId") Integer userId, @Validated User inputUser, Errors errors) {
        // 入力エラーがある場合
        if (errors.hasErrors()) {
            throw new ValidationErrorException(errors);
        }

        // 1件更新する
        inputUser.setId(ID.of(userId));
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
    public Resource delete(@PathVariable("userId") Integer userId) {
        // 1件取得する
        User user = userService.delete(ID.of(userId));

        Resource resource = resourceFactory.create();
        resource.setData(Arrays.asList(user));
        resource.setMessage(getMessage(MESSAGE_SUCCESS));

        return resource;
    }
}
