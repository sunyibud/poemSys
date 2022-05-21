package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.entity.connection.ConUserPoemLike;
import com.poemSys.common.service.ConUserPoemCollectService;
import com.poemSys.common.service.ConUserPoemLikeService;
import com.poemSys.common.service.general.GetLoginSysUserService;
import com.poemSys.user.bean.SysPoemRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理将SysPoem类转化为SysPoemRec(多加了isLike, isCollect)
 */
@Service
public class SwapSysPoemRecService
{
    @Autowired
    ConUserPoemLikeService conUserPoemLikeService;
    
    @Autowired
    ConUserPoemCollectService conUserPoemCollectService;
    
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    
    public SysPoemRes swap(SysPoem sysPoem)
    {
        boolean isLike = false, isCollect = false;
        SysUser sysUser = getLoginSysUserService.getSysUser();
        if(sysUser!=null)
        {
            long userId = sysUser.getId();
            ConUserPoemLike conLike = conUserPoemLikeService.getOne(new QueryWrapper<ConUserPoemLike>()
                    .eq("user_id", userId).eq("poem_id", sysPoem.getId()));
            ConUserPoemCollect conCollect = conUserPoemCollectService.getOne(new QueryWrapper<ConUserPoemCollect>()
                    .eq("user_id", userId).eq("poem_id", sysPoem.getId()));
            if (conLike != null)
                isLike = true;
            if (conCollect != null)
                isCollect = true;
        }
        return new SysPoemRes(sysPoem.getId(), sysPoem.getName(), sysPoem.getContent(), sysPoem.getDynasty(),
                sysPoem.getPoet(), sysPoem.getTranslation(), sysPoem.getNotes(), sysPoem.getAppreciation(),
                sysPoem.getAnalyse(), sysPoem.getBackground(), sysPoem.getLikeNum(), sysPoem.getCollectNum(),
                isLike, isCollect);
    }
}
