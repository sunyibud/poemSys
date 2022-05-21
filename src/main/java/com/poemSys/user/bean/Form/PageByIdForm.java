package com.poemSys.user.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageByIdForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long Id;
    private long page;
    private long size;
}
