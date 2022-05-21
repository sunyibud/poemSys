package com.poemSys.common.security;

import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 重写用户登录方法接口实现
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;
    /**
     * 登录验证，先通过用户名在数据库中取出该用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        if(sysUser ==null)//如果数据库中没找到该用户名
            throw new UsernameNotFoundException("用户名或密码错误");
        try
        {
            LocalDateTime unlockTime = sysUser.getUnlockTime();
            if(unlockTime != null && unlockTime.equals(LocalDateTime.parse("9999-12-31 23:59:59",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            {
                throw new LockedException("用户账号被锁定，无法登录，解封时间：永久锁定!");
            }
            if (unlockTime != null && unlockTime.isAfter(LocalDateTime.now()))
            {
                throw new LockedException("用户账号被锁定，无法登录，解封时间：" + unlockTime);
            }
        }catch (LockedException e)
        {
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
        return new MyUserDetails(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(),
                getUserAuthority(sysUser.getId()));
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId)
    {
        //角色(ROLE_admin)、菜单操作权限sys:user:list
        String authority = sysUserService.getUserAuthorityInfo(userId);//ROLE_admin,sys:user:list,...
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
