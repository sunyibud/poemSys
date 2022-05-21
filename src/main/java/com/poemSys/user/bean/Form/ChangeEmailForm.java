package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeEmailForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String newEmail;
    private String key;
    private String emailCode;
}
