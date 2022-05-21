package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConTagPoem;
import com.poemSys.common.mapper.ConTagPoemMapper;
import com.poemSys.common.service.ConTagPoemService;
import org.springframework.stereotype.Service;

@Service
public class ConTagPoemServiceImpl extends ServiceImpl<ConTagPoemMapper, ConTagPoem> implements ConTagPoemService
{
}
