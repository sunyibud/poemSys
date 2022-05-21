package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.PoetRecommend;
import com.poemSys.common.mapper.PoetRecommendMapper;
import com.poemSys.common.service.PoetRecommendService;
import org.springframework.stereotype.Service;

@Service
public class PoetRecommendServiceImpl extends ServiceImpl<PoetRecommendMapper, PoetRecommend> implements PoetRecommendService
{
}
