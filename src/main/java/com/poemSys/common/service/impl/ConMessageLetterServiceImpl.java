package com.poemSys.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poemSys.common.entity.connection.ConMessageLetter;
import com.poemSys.common.mapper.ConMessageLetterMapper;
import com.poemSys.common.service.ConMessageLetterService;
import org.springframework.stereotype.Service;

@Service
public class ConMessageLetterServiceImpl extends ServiceImpl<ConMessageLetterMapper, ConMessageLetter> implements ConMessageLetterService
{
}
