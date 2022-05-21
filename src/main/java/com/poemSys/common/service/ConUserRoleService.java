package com.poemSys.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poemSys.common.entity.connection.ConUserRole;

public interface ConUserRoleService extends IService<ConUserRole>
{
    void conferAuthority(long id);

    void depriveAuthority(long id);
}
