package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserPost;
import com.poemSys.common.mapper.ConUserPostMapper;
import com.poemSys.common.service.ConUserPostService;
import org.springframework.stereotype.Service;

@Service
public class ConUserPostServiceImpl extends ServiceImpl<ConUserPostMapper, ConUserPost> implements ConUserPostService
{
}
