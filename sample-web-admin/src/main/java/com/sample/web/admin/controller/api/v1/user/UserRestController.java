package com.sample.web.admin.controller.api.v1.user;

import static com.sample.web.base.WebConst.MESSAGE_SUCCESS;

import com.sample.domain.DefaultModelMapperFactory;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.exception.ValidationErrorException;
import com.sample.domain.service.users.UserService;
import com.sample.web.base.controller.api.AbstractRestController;
import com.sample.web.base.controller.api.resource.PageableResource;
import com.sample.web.base.controller.api.resource.PageableResourceImpl;
import com.sample.web.base.controller.api.resource.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController extends AbstractRestController {

  @NonNull final UserService userService;

  @Override
  public String getFunctionName() {
    return "API_USER";
  }

  /**
   * ユーザーを複数取得します。
   *
   * @param request
   * @param pageable
   * @return
   */
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
   * ユーザーを取得します。
   *
   * @param userId
   * @return
   */
  @GetMapping(value = "/{userId}")
  public Resource show(@PathVariable Long userId) {
    val user = userService.findById(userId);
    val resource = resourceFactory.create();
    resource.setContent(List.of(user));
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

    val user = userService.create(inputUser);
    val resource = resourceFactory.create();
    resource.setContent(Arrays.asList(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }

  /**
   * ユーザーを更新します。
   *
   * @param
   */
  @PutMapping(value = "/{userId}")
  public Resource update(
      @PathVariable("userId") Long userId, @Validated @RequestBody User inputUser, Errors errors) {
    // 入力エラーがある場合
    if (errors.hasErrors()) {
      throw new ValidationErrorException(errors);
    }

    inputUser.setId(userId);
    User user;
    {
      // TODO 本来ならサービスで実装すべき
      // created_byなどがAPIからは取得できないので、DBから取得して、パラメータで更新内容を上書きしている
      user = userService.findById(inputUser.getId());
      ModelMapper modelMapper = DefaultModelMapperFactory.create();
      modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
      modelMapper.map(inputUser, user);
    }
    user = userService.update(user);

    val resource = resourceFactory.create();
    resource.setContent(Arrays.asList(user));
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
    val user = userService.delete(userId);

    val resource = resourceFactory.create();
    resource.setContent(Arrays.asList(user));
    resource.setMessage(getMessage(MESSAGE_SUCCESS));

    return resource;
  }
}
