package com.poemSys.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户后台展示的非敏感个人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListInfo
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String signature;
    private String sex;
    private String email;
    private String telephone;
    private String headPath;
    private LocalDateTime registerTime;
    private boolean identity;
    private boolean state;
    private LocalDateTime unlockTime;
//    private String lockAdmin;
}
