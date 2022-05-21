package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysTag;
import com.poemSys.common.mapper.SysTagMapper;
import com.poemSys.common.service.SysTagService;
import org.springframework.stereotype.Service;

@Service
public class SysTagServiceImpl extends ServiceImpl<SysTagMapper, SysTag> implements SysTagService
{
}
