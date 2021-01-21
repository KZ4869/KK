package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.entity.Result;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.CheckItem;
import com.kz.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-23 16:37
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @GetMapping("/findAll")
    public Result findAll() {
        //调用服务
        List<CheckItem> list = checkItemService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
    }

    /**
     * 新增
     *
     * @param checkItem
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
        //调用服务添加
        checkItemService.add(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result deleteById(int id) {

        checkItemService.deleteById(id);

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findById")
    public Result findById(int id) {

        CheckItem checkItem = checkItemService.findById(id);

        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        //调用服务添加
        checkItemService.update(checkItem);
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

}
