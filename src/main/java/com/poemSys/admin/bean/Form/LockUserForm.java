package com.poemSys.admin.bean.Form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockUserForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer lockMinutes;
    private String lockReason;
}
