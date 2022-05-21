package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConUserFollow;
import com.poemSys.common.mapper.ConUserFollowMapper;
import com.poemSys.common.service.ConUserFollowService;
import org.springframework.stereotype.Service;

@Service
public class ConUserFollowServiceImpl extends ServiceImpl<ConUserFollowMapper, ConUserFollow> implements ConUserFollowService
{

}
