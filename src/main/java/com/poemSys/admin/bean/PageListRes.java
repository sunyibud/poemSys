package com.poemSys.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageListRes implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long total;
    private Long size;
    private Long current;
    private Long pages;
    private List<UserListInfo> records;
}
