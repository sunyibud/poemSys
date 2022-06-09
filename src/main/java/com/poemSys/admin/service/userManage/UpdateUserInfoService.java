package com.poemSys.admin.service.userManage;

import com.poemSys.admin.bean.Form.UpdateUserInfoForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.ConUserRoleService;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.UpdateRedisSysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpdateUserInfoService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    UpdateRedisSysUserService updateRedisSysUserService;

    @Autowired
    ConUserRoleService conUserRoleService;

    public Result update(UpdateUserInfoForm newUserInfo)
    {
        long id = newUserInfo.getId();

        if(id==1)
            return new Result(-2, "无法修改系统默认用户信息", null);

        SysUser sysUser = sysUserService.getSysUserById(id);
        if(sysUser==null)
            return new Result(1, "用户id不存在", null);
        String oldUsername = sysUser.getUsername();

        String newUsername = newUserInfo.getUsername();
        //如果改了用户名
        if(!StringUtils.isBlank(newUsername)&&oldUsername.equals(newUsername))
        {
            SysUser sysUser1 = sysUserService.getSysUserByUsername(newUsername);
            if(sysUser1!=null)
                return new Result(1, "用户名已存在，无法修改", null);
        }
        if (!StringUtils.isBlank(newUsername))
            sysUser.setUsername(newUsername);
        if(!StringUtils.isBlank(newUserInfo.getSignature()))
            sysUser.setSignature(newUserInfo.getSignature());
        if(!StringUtils.isBlank(newUserInfo.getSex()))
            sysUser.setSex(newUserInfo.getSex());
        if(!StringUtils.isBlank(newUserInfo.getEmail()))
            sysUser.setEmail(newUserInfo.getEmail());
        if(!StringUtils.isBlank(newUserInfo.getTelephone()))
            sysUser.setTelephone(newUserInfo.getTelephone());

        //更新权限用户关联表
        if(sysUser.isIdentify()&&!newUserInfo.isIdentity())//由管理员改为普通用户
        {
            //从redis中删除用户权限信息，以下次验证权限时从数据库ConUserRole表中重新获取
            sysUserService.clearUserAuthorityInfoByUserId(sysUser.getId());
            conUserRoleService.depriveAuthority(sysUser.getId());
        }
        else if(!sysUser.isIdentify()&&newUserInfo.isIdentity())//由普通用户改为管理员
        {
            sysUserService.clearUserAuthorityInfoByUserId(sysUser.getId());
            conUserRoleService.conferAuthority(sysUser.getId());
        }
        sysUser.setIdentify(newUserInfo.isIdentity());

        String newHeadPath = newUserInfo.getHeadPath();
        if(newHeadPath.equals(""))
            newHeadPath = "/images/headIcons/default.png";
        sysUser.setHeadPath(newHeadPath);
        sysUserService.updateById(sysUser);

        updateRedisSysUserService.updateById(id);
        return new Result(0, "用户信息修改成功", null);
    }
}
