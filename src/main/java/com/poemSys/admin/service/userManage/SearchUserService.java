package com.poemSys.admin.service.userManage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.admin.bean.PageListRes;
import com.poemSys.common.entity.basic.SysUser;
import com.poemSys.common.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SearchUserService
{
    @Autowired
    SysUserService sysUserService;

    @Autowired
    UserInfoPageAnsProcessService userInfoPageAnsProcessService;
    
    public PageListRes search(SearchForm searchUserForm)
    {
        Page<SysUser> userPage = sysUserService.searchByKeyWord(searchUserForm.getKeyWord(),
                searchUserForm.getPage(), searchUserForm.getSize());

        return userInfoPageAnsProcessService.pro(userPage);
    }
}
