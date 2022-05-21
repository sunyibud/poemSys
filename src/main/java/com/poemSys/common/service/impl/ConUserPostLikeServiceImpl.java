package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserPostLike;
import com.poemSys.common.mapper.ConUserPostLikeMapper;
import com.poemSys.common.service.ConUserPostLikeService;
import org.springframework.stereotype.Service;

@Service
public class ConUserPostLikeServiceImpl extends ServiceImpl<ConUserPostLikeMapper, ConUserPostLike> implements ConUserPostLikeService
{
}
