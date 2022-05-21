package com.poemSys.admin.service.userManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.admin.bean.UserListInfo;
import com.poemSys.common.entity.basic.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserInfoPageAnsProcessService
{
    public PageListRes pro(Page<SysUser> page)
    {
        List<UserListInfo> list = new ArrayList<>();

        List<SysUser> records = page.getRecords();

        records.forEach(r -> {
            list.add(new UserListInfo(r.getId(), r.getUsername(),
                    r.getSignature(), r.getSex(), r.getEmail(),
                    r.getTelephone(), r.getHeadPath(), r.getRegisterTime(),
                    r.isIdentify(), r.isState(), r.getUnlockTime()));
        });

        PageListRes ans = new PageListRes(page.getTotal(),
                page.getSize(), page.getCurrent(), page.getPages(),
                list);
        return ans;
    }
}
