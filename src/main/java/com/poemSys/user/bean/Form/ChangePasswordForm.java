package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChangePasswordForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String oldPassword;
    private String newPassword;
}
