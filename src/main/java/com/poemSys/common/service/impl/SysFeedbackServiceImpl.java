package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysFeedback;
import com.poemSys.common.mapper.SysFeedbackMapper;
import com.poemSys.common.service.SysFeedbackService;
import org.springframework.stereotype.Service;

@Service
public class SysFeedbackServiceImpl extends ServiceImpl<SysFeedbackMapper, SysFeedback> implements SysFeedbackService
{
}
