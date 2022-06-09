package com.poemSys.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysDynasty;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.entity.basic.SysTag;
import com.poemSys.common.entity.connection.ConPoetPoem;
import com.poemSys.common.service.*;
import com.poemSys.user.bean.Form.ContentForm;
import com.poemSys.user.bean.Form.NumForm;
import com.poemSys.user.bean.Form.PageByIdForm;
import com.poemSys.user.bean.PoemPageAns;
import com.poemSys.user.bean.SysPoemRes;
import com.poemSys.user.service.general.PoemPageAnsProService;
import com.poemSys.user.service.general.SwapSysPoemResService;
import com.poemSys.user.service.poetryInfo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 古诗文模块接口
 */
@RestController
@RequestMapping("/api/poetry")
public class PoetryInfoController
{
    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    SysTagService sysTagService;

    @Autowired
    SysDynastyService sysDynastyService;

    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    ConPoetPoemService conPoetPoemService;

    @Autowired
    GetNumSentence getNumSentence;

    @Autowired
    GetNumPoemService getNumPoemService;

    @Autowired
    GetNumPoetService getNumPoetService;

    @Autowired
    GetPoetsByDynastyService getPoetsByDynastyService;

    @Autowired
    GetPoemsByTagService getPoemsByTagService;

    @Autowired
    GetPoemsByDynastyService getPoemsByDynastyService;

    @Autowired
    GetPoemsByPoetService getPoemsByPoetService;

    @Autowired
    GetRecPoetsService getRecPoetsService;

    @Autowired
    SwapSysPoemResService swapSysPoemResService;

    @Autowired
    PoemPageAnsProService poemPageAnsProService;

    @Autowired
    SearchPoetryService searchPoetryService;

    @Autowired
    KeywordRecService keyWordRecService;

    @GetMapping("/partPoemList/{page}/{size}")
    public Result partPoemList(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)
    {
        Page<SysPoem> poemPage = new Page<>(page, size);
        Page<SysPoem> pageAns = sysPoemService.page(poemPage);
        PoemPageAns res = poemPageAnsProService.pro(pageAns);
        return new Result(0, "古诗词部分列表获取成功", res);
    }

    @GetMapping("/partPoetList/{page}/{size}")
    public Result partPoetList(@PathVariable("page") Integer page,
                               @PathVariable("size") Integer size)    {
        Page<SysPoet> poetPage = new Page<>(page, size);
        Page<SysPoet> pageAns = sysPoetService.page(poetPage);
        return new Result(0, "诗人部分列表获取成功", pageAns);
    }

    /**
     * 随机获取num个名句
     * @param numForm 数量（超过100个按100）
     */
    @PostMapping("/getNumSentence")
    public Result getNumSentence(@RequestBody NumForm numForm)
    {
        return getNumSentence.get(numForm);
    }

    /**
     * (从推荐古诗库中)随机获取num数量的古诗词信息
     * @param numForm //num超过100时按100计算
     */
    @PostMapping("/getNumPoem")
    public Result getNumPoem(@RequestBody NumForm numForm)
    {
        return getNumPoemService.get(numForm);
    }

    @PostMapping("/getPoemById")
    public Result getPoemById(@RequestBody IdForm idForm)
    {
        SysPoem poem = sysPoemService.getById(idForm.getId());
        if(poem==null)
            return new Result(1, "没有查询到古诗词,id:"+idForm.getId(), null);
        SysPoemRes poemRes = swapSysPoemResService.swap(poem);
        return new Result(0, "古诗词信息获取成功,id:"+idForm.getId(), poemRes);
    }

    /**
     * (从推荐诗人库中）随机获取num数量的诗人信息
     * @param numForm //num超过100时按100计算
     */
    @PostMapping("/getNumPoet")
    public Result getNumPoet(@RequestBody NumForm numForm)
    {
        return getNumPoetService.get(numForm);
    }

    /**
     * 获取推荐的100个诗人
     */
    @PostMapping("/getRecPoets")
    public Result getRecPoets()
    {
        return getRecPoetsService.get();
    }

    @PostMapping("/getPoetById")
    public Result getPoetById(@RequestBody IdForm idForm)
    {
        SysPoet poet = sysPoetService.getById(idForm.getId());
        if(poet==null)
            return new Result(1, "没有查询到诗人,id:"+idForm.getId(), null);
        return new Result(0, "诗人信息获取成功,id:"+idForm.getId(), poet);
    }

    @PostMapping("/getPoetByPoemId")
    public Result getPoetByPoemId(@RequestBody IdForm idForm)
    {
        long poemId = idForm.getId();
        ConPoetPoem conPoetPoem = conPoetPoemService.getOne(new QueryWrapper<ConPoetPoem>()
                .eq("poem_id", poemId));
        if(conPoetPoem==null)
            return new Result(0, "暂无诗人信息,poemId:"+poemId, null);
        SysPoet sysPoet = sysPoetService.getById(conPoetPoem.getPoetId());
        return new Result(0, "诗人信息获取成功,poemId:"+poemId, sysPoet);
    }

    @PostMapping("/getPoetsByDynasty")
    public Result getPoetsByDynasty(@RequestBody PageByIdForm pageByIdForm)
    {
        return getPoetsByDynastyService.get(pageByIdForm);
    }

    /**
     * 获取古诗词标签列表（所有的标签）
     */
    @PostMapping("/getTagList")
    public Result getTagList()
    {
        List<SysTag> list = sysTagService.list();
        return new Result(0, "获取标签成功,共"+list.size()+"条", list);
    }

    /**
     * 分页获取某一种标签下的所有古诗词
     */
    @PostMapping("/getPoemsByTag")
    public Result getPoemsByTag(@RequestBody PageByIdForm pageByIdForm)
    {
        return getPoemsByTagService.get(pageByIdForm);
    }

    @PostMapping("/getDynastyList")
    public Result getDynastyList()
    {
        List<SysDynasty> list = sysDynastyService.list();
        return new Result(0, "朝代列表获取成功,共"+list.size()+"条", list);
    }

    @PostMapping("/getPoemsByDynasty")
    public Result getPoemsByDynasty(@RequestBody PageByIdForm pageByIdForm)
    {
        return getPoemsByDynastyService.get(pageByIdForm);
    }

    @PostMapping("/getPoetList")
    public Result getPoetList()
    {
        List<SysPoet> list = sysPoetService.list();
        return new Result(0, "诗人列表获取成功,共"+list.size()+"条", list);
    }

    @PostMapping("/getPoemsByPoet")
    public Result getPoemsByPoet(@RequestBody PageByIdForm pageByIdForm)
    {
        return getPoemsByPoetService.get(pageByIdForm);
    }

    @PostMapping("/keywordRec")
    public Result keywordRec(@RequestBody ContentForm contentForm)
    {
        return keyWordRecService.get(contentForm);
    }


    /**
     * 搜索古诗词有关信息,分页获取结果
     */
    @PostMapping("/searchPoetry")
    public Result searchPoetry(@RequestBody SearchForm searchForm)
    {
        return searchPoetryService.search(searchForm);
    }

}