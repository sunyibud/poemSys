package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.mapper.SysPoetMapper;
import com.poemSys.common.service.SysPoetService;
import org.springframework.stereotype.Service;

@Service
public class SysPoetServiceImpl extends ServiceImpl<SysPoetMapper, SysPoet> implements SysPoetService
{
}
