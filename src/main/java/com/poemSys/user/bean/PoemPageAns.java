package com.poemSys.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 古诗词分页结果封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemPageAns implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long total;
    private Long size;
    private Long current;
    private Long pages;
    private List<SysPoemRes> records;
}
