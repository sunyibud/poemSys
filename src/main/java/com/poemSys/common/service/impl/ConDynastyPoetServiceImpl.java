package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConDynastyPoet;
import com.poemSys.common.mapper.ConDynastyPoetMapper;
import com.poemSys.common.service.ConDynastyPoetService;
import org.springframework.stereotype.Service;

@Service
public class ConDynastyPoetServiceImpl extends ServiceImpl<ConDynastyPoetMapper, ConDynastyPoet> implements ConDynastyPoetService
{
}
