package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysDynasty;
import com.poemSys.common.mapper.SysDynastyMapper;
import com.poemSys.common.service.SysDynastyService;
import org.springframework.stereotype.Service;

@Service
public class SysDynastyServiceImpl extends ServiceImpl<SysDynastyMapper, SysDynasty> implements SysDynastyService
{
}
