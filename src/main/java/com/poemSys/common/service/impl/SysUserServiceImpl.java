package com.poemSys.common.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysRole;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.mapper.SysUserMapper;
import com.poemSys.common.service.SysRoleService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService
{
    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public SysUser getSysUserByUsername(String username)
    {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId)
    {
        String authority = "";


        if(redisUtil.hasKey("GrantedAuthority:"+userId))
        {
            authority = (String) redisUtil.get("GrantedAuthority:" + userId);
        }
        else
        {
            //获取角色编码
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from con_user_role where user_id = " + userId));
            if (roles.size() > 0)
            {
                authority = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
            }
            //获取菜单操作编码
            //...


            //将认证权限信息缓存到redis中，避免无必要的读取数据库
            redisUtil.set("GrantedAuthority:" + userId, authority, 60 * 60);//60分钟
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfoByUserId(Long userId)
    {
        redisUtil.del("GrantedAuthority:"+userId);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId)
    {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>().inSql("id",
                "select user_id from con_user_role where role_id= " + roleId));
        sysUsers.forEach(u -> this.clearUserAuthorityInfoByUserId(u.getId()));
    }

    @Override
    public void deleteSysUserById(Long id)
    {
        if(id==1)
            return;
        this.clearRedisSysUserById(id);
        this.removeById(id);
    }

    @Override
    public void updateUserState()
    {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>().eq("state", false));
        sysUsers.forEach(u ->{
            if(u.getUnlockTime()!=null)
            {
                if (u.getUnlockTime().isBefore(LocalDateTime.now()))
                {
                    u.setState(true);
                    u.setUnlockTime(null);
                    u.setLockReason(null);
                    u.setLockAdmin(null);
                }
                this.updateById(u);
            }
        });
    }

    @Override
    public Page<SysUser> searchByKeyWord(String keyWord, Long page, Long size)
    {
        this.updateUserState();

        Page<SysUser> userPage = new Page<>(page, size);

        //如果关键词为空查询所有
        if(StringUtils.isBlank(keyWord))
            return this.page(userPage);

        //不为空，分页查询结果
        return this.page(userPage,
                new QueryWrapper<SysUser>()
                        .like("username", keyWord)
                        .or().like("signature", keyWord)
                        .or().like("sex", keyWord)
                        .or().like("email", keyWord)
                        .or().like("telephone", keyWord)
                        .or().like("register_time", keyWord)
        );
    }

    @Override
    public boolean isEmailExist(String emailAddress)
    {
        SysUser email = getOne(new QueryWrapper<SysUser>().eq("email", emailAddress));
        return email != null;
    }

    //根据id获取用户信息字段（用redis优化）
    @Override
    public SysUser getSysUserById(Serializable id)
    {
        //查看redis中是否缓存
        if(redisUtil.hasKey("sysUser:"+id))
        {
            String jsonStr = (String) redisUtil.get("sysUser:" + id);
            return JSONUtil.toBean(jsonStr, SysUser.class);
        }
        else
        {
            SysUser sysUser = this.getById(id);
            //将对象转化成json再存入redis
            redisUtil.set("sysUser:" + id,
                    JSONUtil.toJsonStr(sysUser), 60 * 60 * 24 * 10);//10天
            return sysUser;
        }
    }

    @Override
    public void clearRedisSysUserById(Serializable id)
    {
        redisUtil.del("sysUser:"+id);
    }
}
