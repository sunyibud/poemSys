package com.poemSys.user.service.userInfo;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Slf4j
@Service
public class UploadHeadIconService
{
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    SysUserService sysUserService;

    public Result save(MultipartFile file)
    {
        if (file==null)
            return new Result(1, "文件上传为空或接收异常", null);
        try {
            long bytes = file.getSize();
            long megabytes = bytes / 1024 / 1024;
            if(megabytes>10)
                return new Result(-3, "图片大小不能超过10M", null);
            String filePath = "/root/dist/images/headIcons/";
            File folder = new File(filePath);
            if(!folder.exists())
            {
                if(!folder.mkdirs())
                    return new Result(-1, "服务器异常，头像文件夹创建失败", null);
            }
            String oldName = file.getOriginalFilename();
            String fileType =  oldName.split("\\.")[1];
            if(!fileType.contains("jpg") && !fileType.contains("png")
                &&!fileType.contains("jpeg"))
                return new Result(-3, "图片格式不支持，仅支持jpg/jpeg/png", null);

            String newName = UUID.randomUUID().toString()+
                    oldName.substring(oldName.lastIndexOf("."));
            file.transferTo(new File(folder, newName));
            String newHeadPath = "/images/headIcons/"+newName;

            //保存新用户头像地址到数据库
            SysUser sysUser = getLoginSysUserService.getSysUser();
            sysUser.setHeadPath(newHeadPath);
            sysUserService.updateById(sysUser);

            log.info("上传图片路径： /images/headIcons/"+newName);
            return new Result(0, "上传成功", newHeadPath);
        } catch (Exception e) {
            return new Result(-1, "出现错误，上传失败", e.getMessage());
        }

    }
}
