package com.poemSys.user.service.general;

import com.poemSys.common.bean.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * 图片上传
 */
@Slf4j
@Service
public class ImageUploadService
{
    /**
     * @param file 二进制文件
     * @param filePath 图片上传目标路径（不需要前缀"/root/dist"）
     */
    public Result upload(MultipartFile file, String filePath)
    {
        String basePath = "/root/dist";
        filePath += basePath;
        if (file==null)
            return new Result(1, "文件上传为空或接收异常", null);
        try {
            long bytes = file.getSize();
            long megabytes = bytes / 1024 / 1024;
            if(megabytes>10)
                return new Result(-3, "图片大小不能超过10M", null);
            File folder = new File(filePath);
            if(!folder.exists())
            {
                if(!folder.mkdirs())
                    return new Result(-1, "服务器异常，头像文件夹创建失败", null);
            }
            String oldName = file.getOriginalFilename();
            if(StringUtils.isBlank(oldName))
                return new Result(-3, "上传文件格式存在问题", null);
            String fileType =  oldName.split("\\.")[1];
            if(!fileType.contains("jpg") && !fileType.contains("png")
                    &&!fileType.contains("jpeg"))
                return new Result(-3, "图片格式不支持，仅支持jpg/jpeg/png", null);

            String newName = UUID.randomUUID().toString()+
                    oldName.substring(oldName.lastIndexOf("."));
            file.transferTo(new File(folder, newName));
            String savaFilePath = filePath+newName;

            log.info("上传图片路径： "+savaFilePath);
            return new Result(0, "上传成功", savaFilePath);
        } catch (Exception e) {
            return new Result(-1, "出现错误，上传失败", e.getMessage());
        }

    }
}
