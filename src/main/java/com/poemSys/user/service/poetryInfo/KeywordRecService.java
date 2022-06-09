package com.poemSys.user.service.poetryInfo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysKeyWordRec;
import com.poemSys.common.service.SysKeyWordRecService;
import com.poemSys.user.bean.Form.ContentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 在搜索框中输入时动态给出推荐的关键字（10个）
 */
@Service
public class KeywordRecService
{
    @Autowired
    SysKeyWordRecService sysKeyWordRecService;

    public Result get(ContentForm contentForm)
    {
        String keyWord = contentForm.getContent();

        List<SysKeyWordRec> keyWordRecs = sysKeyWordRecService.list(new QueryWrapper<SysKeyWordRec>()
                .likeRight("key_word", keyWord));

        List<String> res = new ArrayList<>();
        int num = 10;
        for (SysKeyWordRec keyWordRec : keyWordRecs)
        {
            if(num == 0)
                break;
            res.add(keyWordRec.getKeyWord());
            num -- ;
        }

        return new Result(0, "关键字匹配结果", res);
    }
}
