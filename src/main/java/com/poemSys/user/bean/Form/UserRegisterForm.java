package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String sex;
    private String email;
    private String key;
    private String emailCode;
    private String telephone;
}
