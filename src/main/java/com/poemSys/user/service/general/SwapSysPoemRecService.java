package com.poemSys.user.service.general;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.entity.connection.ConUserPoemLike;
import com.poemSys.common.service.ConUserPoemCollectService;
import com.poemSys.common.service.ConUserPoemLikeService;
import com.poemSys.user.bean.SysPoemRes;
import com.poemSys.user.service.poetryInfo.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处理将SysPoem类转化为SysPoemRec(多加了isLike, isCollect)
 */
@Slf4j
@Service
public class SwapSysPoemRecService
{
    @Autowired
    ConUserPoemLikeService conUserPoemLikeService;
    
    @Autowired
    ConUserPoemCollectService conUserPoemCollectService;
    
    @Autowired
    GetLoginSysUserService getLoginSysUserService;

    @Autowired
    QRCodeService qrCodeService;

    public SysPoemRes swap(SysPoem sysPoem)
    {
        boolean isLike = false, isCollect = false;
        SysUser sysUser = getLoginSysUserService.getSysUser();
        Long poemId = sysPoem.getId();
        if (sysUser != null)   //处理是否点赞和收藏
        {
            long userId = sysUser.getId();
            ConUserPoemLike conLike = conUserPoemLikeService.getOne(new QueryWrapper<ConUserPoemLike>()
                    .eq("user_id", userId).eq("poem_id", poemId));
            ConUserPoemCollect conCollect = conUserPoemCollectService.getOne(new QueryWrapper<ConUserPoemCollect>()
                    .eq("user_id", userId).eq("poem_id", poemId));
            if (conLike != null)
                isLike = true;
            if (conCollect != null)
                isCollect = true;
        }

        //生成二维码
        String mobileWebUrl = "http://hackcode.ishoulu.com/scp/" + poemId;
        String QRCodeBase64Img = null;
        try
        {
            QRCodeBase64Img = qrCodeService.crateQRCode(mobileWebUrl);
        } catch (Exception e)
        {
            log.error("生成古诗词二维码出错");
        }

        return new SysPoemRes(poemId, sysPoem.getName(), sysPoem.getContent(), sysPoem.getDynasty(),
                sysPoem.getPoet(), sysPoem.getTranslation(), sysPoem.getNotes(), sysPoem.getAppreciation(),
                sysPoem.getAnalyse(), sysPoem.getBackground(), sysPoem.getLikeNum(), sysPoem.getCollectNum(),
                isLike, isCollect, QRCodeBase64Img);
    }
}
