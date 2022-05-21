package com.poemSys.user.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String title;
    private String content;
    private String cover;
}
