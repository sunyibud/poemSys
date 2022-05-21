package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConPoetPoem;
import com.poemSys.common.mapper.ConPoetPoemMapper;
import com.poemSys.common.service.ConPoetPoemService;
import org.springframework.stereotype.Service;

@Service
public class ConPoetPoemServiceImpl extends ServiceImpl<ConPoetPoemMapper, ConPoetPoem> implements ConPoetPoemService
{
}
