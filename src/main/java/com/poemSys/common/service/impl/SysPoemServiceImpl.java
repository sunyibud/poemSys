package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.mapper.SysPoemMapper;
import com.poemSys.common.service.SysPoemService;
import org.springframework.stereotype.Service;

@Service
public class SysPoemServiceImpl extends ServiceImpl<SysPoemMapper, SysPoem> implements SysPoemService
{
}
