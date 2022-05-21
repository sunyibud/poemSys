package com.poemSys.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoetInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String dynasty;
    private String introduce;
}
