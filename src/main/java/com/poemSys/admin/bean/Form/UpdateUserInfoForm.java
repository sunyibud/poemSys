package com.poemSys.admin.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String signature;
    private String sex;
    private String email;
    private String telephone;
    private String headPath;
    private boolean identity;
}