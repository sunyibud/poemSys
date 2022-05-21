package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserRole;
import com.poemSys.common.mapper.ConUserRoleMapper;
import com.poemSys.common.service.ConUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class ConUserRoleServiceImpl extends ServiceImpl<ConUserRoleMapper, ConUserRole> implements ConUserRoleService
{
    /**
     * 为某个非管理员用户赋予普通管理员权限(admin)
     * @param userId
     */
    @Override
    public void conferAuthority(long userId)
    {
        this.save(new ConUserRole(userId, 2));
    }

    /**
     * 剥夺某个管理员用户的管理员权限(admin)
     * @param userId
     */
    @Override
    public void depriveAuthority(long userId)
    {
        this.remove(new QueryWrapper<ConUserRole>().eq("user_id", userId)
        .eq("role_id", 2));
    }
}
