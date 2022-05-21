package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.basic.SysLetter;
import com.poemSys.common.mapper.SysLetterMapper;
import com.poemSys.common.service.SysLetterService;
import org.springframework.stereotype.Service;

@Service
public class SysLetterServiceImpl extends ServiceImpl<SysLetterMapper, SysLetter> implements SysLetterService
{
}
