package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConPostComment;
import com.poemSys.common.mapper.ConPostCommentMapper;
import com.poemSys.common.service.ConPostCommentService;
import org.springframework.stereotype.Service;

@Service
public class ConPostCommentServiceImpl extends ServiceImpl<ConPostCommentMapper, ConPostComment> implements ConPostCommentService
{
}
