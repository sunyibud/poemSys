package com.poemSys.admin.service.poemManage;

import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.service.SysPoemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdatePoemInfoService
{
    @Autowired
    SysPoemService sysPoemService;

    public Result update(SysPoem newSysPoem)
    {
        long id = newSysPoem.getId();
        SysPoem sysPoem = sysPoemService.getById(id);
        if(!StringUtils.isBlank(newSysPoem.getName()))
            sysPoem.setName(newSysPoem.getName());
        if(!StringUtils.isBlank(newSysPoem.getContent()))
            sysPoem.setContent(newSysPoem.getContent());
        if(!StringUtils.isBlank(newSysPoem.getDynasty()))
            sysPoem.setDynasty(newSysPoem.getDynasty());
        if(!StringUtils.isBlank(newSysPoem.getPoet()))
            sysPoem.setPoet(newSysPoem.getPoet());
        if(!StringUtils.isBlank(newSysPoem.getTranslation()))
            sysPoem.setTranslation(newSysPoem.getTranslation());
        if(!StringUtils.isBlank(newSysPoem.getNotes()))
            sysPoem.setNotes(newSysPoem.getNotes());
        if(!StringUtils.isBlank(newSysPoem.getAppreciation()))
            sysPoem.setAppreciation(newSysPoem.getAppreciation());
        if(!StringUtils.isBlank(newSysPoem.getAnalyse()))
            sysPoem.setAnalyse(newSysPoem.getAnalyse());
        if(!StringUtils.isBlank(newSysPoem.getBackground()))
            sysPoem.setBackground(newSysPoem.getBackground());
        sysPoemService.updateById(sysPoem);
        return new Result(0, "古诗词信息修改成功", null);
    }
}
