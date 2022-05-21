package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class FindPasswordForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String emailAddress;
    private String key;
    private String emailCode;
    private String newPassword;
}
