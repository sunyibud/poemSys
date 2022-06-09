package com.poemSys.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poemSys.common.entity.basic.SysUser;

import java.io.Serializable;
import java.util.List;

public interface SysUserService extends IService<SysUser>
{
    SysUser getSysUserByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    void clearUserAuthorityInfoByUserId(Long userId);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void deleteSysUserById(Long id);

    void updateUserState();

    Page<SysUser> searchByKeyWord(String keyWord, Long page, Long size);

    boolean isEmailExist(String emailAddress);

    SysUser getSysUserById(Serializable id);

    void clearRedisSysUserById(Serializable id);
}
