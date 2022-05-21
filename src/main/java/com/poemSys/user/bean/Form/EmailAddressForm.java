package com.poemSys.user.bean.Form;

import lombok.Data;

import java.io.Serializable;
@Data
public class EmailAddressForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String emailAddress;
}
