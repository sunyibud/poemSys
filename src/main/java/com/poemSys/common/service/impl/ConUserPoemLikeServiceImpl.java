package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserPoemLike;
import com.poemSys.common.mapper.ConUserPoemLikeMapper;
import com.poemSys.common.service.ConUserPoemLikeService;
import org.springframework.stereotype.Service;

@Service
public class ConUserPoemLikeServiceImpl extends ServiceImpl<ConUserPoemLikeMapper, ConUserPoemLike> implements ConUserPoemLikeService
{
}
