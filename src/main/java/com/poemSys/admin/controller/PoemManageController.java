package com.poemSys.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poemSys.admin.bean.Form.IdForm;
import com.poemSys.admin.bean.Form.IdsForm;
import com.poemSys.admin.bean.Form.SearchForm;
import com.poemSys.admin.bean.PoemInfo;
import com.poemSys.admin.service.poemManage.UpdatePoemInfoService;
import com.poemSys.common.bean.Result;
import com.poemSys.common.entity.basic.SysPoem;
import com.poemSys.common.service.SysPoemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台古诗词管理模块
 */
@RestController
@RequestMapping("/api/admin")
public class PoemManageController
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    SysPoemService sysPoemService;

    @Autowired
    UpdatePoemInfoService updatePoemInfoService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/addPoem")
    public Result addPoem(@RequestBody PoemInfo poemInfo)
    {
        SysPoem newSysPoem = new SysPoem(poemInfo.getName(),
                poemInfo.getContent(), poemInfo.getDynasty(), poemInfo.getPoet(),
                poemInfo.getTranslation(), poemInfo.getNotes(),
                poemInfo.getAppreciation(), poemInfo.getAnalyse(),
                poemInfo.getBackground(), 0, 0);
        sysPoemService.save(newSysPoem);
        return new Result(0, "古诗词信息添加成功", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePoem")
    public Result deletePoem(@RequestBody IdForm idForm)
    {
        long id = idForm.getId();
        sysPoemService.removeById(id);
        return new Result(0, "古诗词信息删除成功，id:" + id, null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/deletePoems")
    public Result deletePoems(@RequestBody IdsForm idsForm)
    {
        List<IdForm> poemIds = idsForm.getIds();
        poemIds.forEach(poemId -> {
            sysPoemService.removeById(poemId.getId());
        });
        return new Result(0, "用户批量删除成功,共删除"+poemIds.size()+"条数据", null);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/searchPoem")
    public Result searchPoem(@RequestBody SearchForm searchPoemForm)
    {
        Page<SysPoem> poemPage = new Page<>(searchPoemForm.getPage(), searchPoemForm.getSize());
        if (StringUtils.isBlank(searchPoemForm.getKeyWord()))
        {
            Page<SysPoem> allPageAns = sysPoemService.page(poemPage);
            return new Result(0, "古诗词信息查询成功，共" + allPageAns.getTotal() + "条数据", allPageAns);
        }
        String keyWord = searchPoemForm.getKeyWord();
        Page<SysPoem> pageAns = sysPoemService.page(poemPage,
                new QueryWrapper<SysPoem>()
                        .like("id", keyWord)
                        .or().like("name", keyWord)
                        .or().like("content", keyWord)
                        .or().like("dynasty", keyWord)
                        .or().like("poet", keyWord)
                        .or().like("translation", keyWord)
                        .or().like("notes", keyWord)
                        .or().like("appreciation", keyWord)
                        .or().like("analyse", keyWord)
                        .or().like("background", keyWord)
        );
        return new Result(0, "古诗词信息查询成功，共" + pageAns.getTotal() + "条数据", pageAns);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/updatePoem")
    public Result updatePoem(@RequestBody SysPoem sysPoem)
    {
        return updatePoemInfoService.update(sysPoem);
    }
}
