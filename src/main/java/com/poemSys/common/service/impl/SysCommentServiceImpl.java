package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysComment;
import com.poemSys.common.mapper.SysCommentMapper;
import com.poemSys.common.service.SysCommentService;
import org.springframework.stereotype.Service;

@Service
public class SysCommentServiceImpl extends ServiceImpl<SysCommentMapper, SysComment> implements SysCommentService
{
}
