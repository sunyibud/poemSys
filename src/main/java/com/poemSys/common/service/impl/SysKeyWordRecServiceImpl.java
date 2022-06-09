package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysKeyWordRec;
import com.poemSys.common.mapper.SysKeyWordRecMapper;
import com.poemSys.common.service.SysKeyWordRecService;
import org.springframework.stereotype.Service;

@Service
public class SysKeyWordRecServiceImpl extends ServiceImpl<SysKeyWordRecMapper, SysKeyWordRec> implements SysKeyWordRecService
{
}
