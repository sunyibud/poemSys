package com.poemSys.admin.bean.Form;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long page;
    private Long size;
    private String keyword;
}
