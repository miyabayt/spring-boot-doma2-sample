package com.bigtreetc.sample.web.api.controller.v1;

import static com.bigtreetc.sample.web.base.WebConst.MESSAGE_SUCCESS;

import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import com.bigtreetc.sample.domain.exception.ValidationErrorException;
import com.bigtreetc.sample.domain.service.user.UserService;
import com.bigtreetc.sample.web.api.base.AbstractRestController;
import com.bigtreetc.sample.web.api.base.resource.PageableResource;
import com.bigtreetc.sample.web.api.base.resource.PageableResourceImpl;
import com.bigtreetc.sample.web.api.base.resource.Resource;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends AbstractRestController {

  @NonNull final UserService userService;

  @Override
  public String getFunctionName() {
    return "API_USER";
  }

  /**
   * 顧客マスタを検索します。
   *
   * @param request
   * @param pageable
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping
  public PageableResource index(SearchUserRequest request, Pageable pageable) throws IOException {
    // 入力値からDTOを作成する
    val criteria = modelMapper.map(request, UserCriteria.class);

    // 10件で区切って取得する
    val users = userService.findAll(criteria, pageable);
    val resource = modelMapper.map(users, PageableResourceImpl.class);
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }

  /**
   * 顧客マスタを取得します。
   *
   * @param userId
   * @return
   */
  @PreAuthorize("hasAuthority('user:read')")
  @GetMapping(value = "/{userId}")
  public Resource show(@PathVariable Long userId) {
    val user = userService.findById(userId);

    val resource = resourceFactory.create();
    resource.setContent(List.of(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }

  /**
   * 顧客マスタを登録します。
   *
   * @param
   */
  @PreAuthorize("hasAuthority('user:save')")
  @PostMapping
  public Resource create(@Validated @RequestBody User inputUser, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    val user = userService.create(inputUser);

    val resource = resourceFactory.create();
    resource.setContent(List.of(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }

  /**
   * 顧客マスタを更新します。
   *
   * @param
   */
  @PreAuthorize("hasAuthority('user:save')")
  @PutMapping(value = "/{userId}")
  public Resource update(
      @PathVariable("userId") Long userId, @Validated @RequestBody User inputUser, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    inputUser.setId(userId);
    val user = userService.update(inputUser);

    val resource = resourceFactory.create();
    resource.setContent(List.of(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }

  /**
   * 顧客マスタを削除します。
   *
   * @param
   */
  @PreAuthorize("hasAuthority('user:save')")
  @DeleteMapping(value = "/{userId}")
  public Resource delete(@PathVariable("userId") Long userId) {
    val user = userService.delete(userId);

    val resource = resourceFactory.create();
    resource.setContent(List.of(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }
}
