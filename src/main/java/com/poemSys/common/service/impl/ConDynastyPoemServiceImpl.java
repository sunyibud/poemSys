package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConDynastyPoem;
import com.poemSys.common.mapper.ConDynastyPoemMapper;
import com.poemSys.common.service.ConDynastyPoemService;
import org.springframework.stereotype.Service;

@Service
public class ConDynastyPoemServiceImpl extends ServiceImpl<ConDynastyPoemMapper, ConDynastyPoem> implements ConDynastyPoemService
{
}
