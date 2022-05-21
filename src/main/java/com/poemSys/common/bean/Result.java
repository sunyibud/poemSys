package com.poemSys.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private Object data;
}
