package com.poemSys.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poemSys.common.entity.basic.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser>
{
    SysUser getSysUserByUsername(String username);

    SysUser getSysUserById(Long userId);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfoByUserId(Long userId);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void deleteSysUserById(Long id);

    void updateUserState();

    Page<SysUser> searchByKeyWord(String keyWord, Long page, Long size);

    boolean isEmailExist(String emailAddress);
}
