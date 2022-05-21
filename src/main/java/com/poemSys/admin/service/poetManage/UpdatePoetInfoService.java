package com.poemSys.admin.service.poetManage;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.service.SysPoetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdatePoetInfoService
{
    @Autowired
    SysPoetService sysPoetService;

    public Result update(SysPoet newSysPoet)
    {
        long id = newSysPoet.getId();
        SysPoet sysPoet = sysPoetService.getById(id);
        if(!StringUtils.isBlank(newSysPoet.getName()))
            sysPoet.setName(newSysPoet.getName());
        if(!StringUtils.isBlank(newSysPoet.getIntroduce()))
            sysPoet.setIntroduce(newSysPoet.getIntroduce());
        sysPoetService.updateById(sysPoet);
        return new Result(0, "诗人信息修改成功", null);
    }
}
