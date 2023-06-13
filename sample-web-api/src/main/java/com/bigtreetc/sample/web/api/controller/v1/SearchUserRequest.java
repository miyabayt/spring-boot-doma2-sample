package com.bigtreetc.sample.web.api.controller.v1;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchUserRequest implements Serializable {

  private static final long serialVersionUID = 7593564324192730932L;

  String email;

  String firstName;

  String lastName;

  String tel;

  String zip;

  String address;
}
