package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.PoemRecommend;
import com.poemSys.common.mapper.PoemRecommendMapper;
import com.poemSys.common.service.PoemRecommendService;
import org.springframework.stereotype.Service;

@Service
public class PoemRecommendServiceImpl extends ServiceImpl<PoemRecommendMapper, PoemRecommend> implements PoemRecommendService
{
}
