package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserPoemCollect;
import com.poemSys.common.mapper.ConUserPoemCollectMapper;
import com.poemSys.common.service.ConUserPoemCollectService;
import org.springframework.stereotype.Service;

@Service
public class ConUserPoemCollectServiceImpl extends ServiceImpl<ConUserPoemCollectMapper, ConUserPoemCollect> implements ConUserPoemCollectService
{
}
