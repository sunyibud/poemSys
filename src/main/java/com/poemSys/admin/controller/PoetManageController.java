package com.poemSys.admin.controller;

import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.IdsForm;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.admin.bean.PoetInfo;
import com.poemSys.admin.service.poetManage.SearchPoetService;
import com.poemSys.admin.service.poetManage.UpdatePoetInfoService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.service.SysPoetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 后台诗人管理模块
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class PoetManageController
{
    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    UpdatePoetInfoService updatePoetInfoService;

    @Autowired
    SearchPoetService searchPoetService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/addPoet")
    public Result addPoet(@RequestBody PoetInfo poetInfo)
    {
        SysPoet newSysPoet = new SysPoet(poetInfo.getName(),
                poetInfo.getDynasty(), poetInfo.getIntroduce(), null);
        sysPoetService.save(newSysPoet);
        return new Result(0, "诗人信息添加成功", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePoet")
    public Result deletePoet(@RequestBody IdForm idForm)
    {
        long id = idForm.getId();
        log.info("id"+id);
        sysPoetService.removeById(id);
        return new Result(0, "诗人信息删除成功，id:"+id, null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePoets")
    public Result deletePoets(@RequestBody IdsForm idsForm)
    {
        List<IdForm> poetIds = idsForm.getIds();
        poetIds.forEach(poetId -> sysPoetService.removeById(poetId.getId()));
        return new Result(0, "诗人批量删除成功,共删除"+poetIds.size()+"条数据", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/searchPoet")
    public Result searchPoet(@RequestBody SearchForm searchForm)
    {
       return searchPoetService.search(searchForm);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/updatePoet")
    public Result updatePoet(@RequestBody SysPoet sysPoet)
    {
        return updatePoetInfoService.update(sysPoet);
    }
}
