package com.plete.basic.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserCreateForm {

    @NotEmpty
    @Email
    private String username;

    @Size(min = 8)
    @NotEmpty
    private String password;


}