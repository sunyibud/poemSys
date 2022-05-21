package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysSentence;
import com.poemSys.common.mapper.SysSentenceMapper;
import com.poemSys.common.service.SysSentenceService;
import org.springframework.stereotype.Service;

@Service
public class SysSentenceServiceImpl extends ServiceImpl<SysSentenceMapper, SysSentence> implements SysSentenceService
{
}
