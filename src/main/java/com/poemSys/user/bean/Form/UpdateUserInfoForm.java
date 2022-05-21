package com.poemSys.user.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String username;
    private String signature;
    private String sex;
    private String telephone;
}
