package com.poemSys.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsOk implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean ok;
}
