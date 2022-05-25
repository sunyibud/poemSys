package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.user.service.general.GetLoginSysUserService;
import com.poemSys.user.service.general.UpdateRedisLoginSysUserService;
import com.poemSys.user.service.general.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadHeadIconService
{
    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    UpdateRedisLoginSysUserService updateRedisLoginSysUserService;

    public Result upload(MultipartFile file)
    {
        Result result = imageUploadService.upload(file, "/images/headIcons/");
        if(result.getCode()==0)//保存到数据库中
        {
            SysUser sysUser = getLoginSysUserService.getSysUser();
            sysUser.setHeadPath(result.getData().toString());
            sysUserService.updateById(sysUser);
            updateRedisLoginSysUserService.update();
        }
        return result;
    }
}
