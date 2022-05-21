package com.poemSys.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.IdsForm;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.admin.bean.PoetInfo;
import com.poemSys.admin.service.poetManage.UpdatePoetInfoService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoet;
import com.poemSys.common.service.SysPoetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    HttpServletRequest request;

    @Autowired
    SysPoetService sysPoetService;

    @Autowired
    UpdatePoetInfoService updatePoetInfoService;


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
        poetIds.forEach(poetId -> {
            sysPoetService.removeById(poetId.getId());
        });
        return new Result(0, "诗人批量删除成功,共删除"+poetIds.size()+"条数据", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/searchPoet")
    public Result searchPoet(@RequestBody SearchForm searchPoetForm)
    {
        Page<SysPoet> poetPage = new Page<>(searchPoetForm.getPage(), searchPoetForm.getSize());
        if(StringUtils.isBlank(searchPoetForm.getKeyWord()))
        {
            Page<SysPoet> allPageAns = sysPoetService.page(poetPage);
            return new Result(0, "诗人信息查询成功，共"+allPageAns.getTotal()+"条数据", allPageAns);
        }
        String keyWord = searchPoetForm.getKeyWord();
        Page<SysPoet> pageAns = sysPoetService.page(poetPage,
                new QueryWrapper<SysPoet>()
                        .like("id", keyWord)
                        .or().like("name", keyWord)
                        .or().like("dynasty", keyWord)
                        .or().like("introduce", keyWord)
        );
        return new Result(0, "诗人信息查询成功，共"+pageAns.getTotal()+"条数据", pageAns);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/updatePoet")
    public Result updatePoet(@RequestBody SysPoet sysPoet)
    {
        return updatePoetInfoService.update(sysPoet);
    }
}
