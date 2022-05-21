package com.poemSys.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoemInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String name;
    private String content;
    private String dynasty;
    private String poet;
    private String translation;
    private String notes;
    private String appreciation;
    private String analyse;
    private String background;
}
