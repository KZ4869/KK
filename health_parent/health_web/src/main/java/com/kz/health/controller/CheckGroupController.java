package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.entity.Result;
import com.kz.health.pojo.CheckGroup;
import com.kz.health.service.CheckGroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-27 23:35
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        //调用业务服务
        checkGroupService.add(checkGroup,checkitemIds);
        //响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用业务服务
        PageResult<CheckGroup> pageResult =  checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }

    /**
     *
     * @RequestParam 当参数与传递的键名不一致时
     * @param groupId
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam("checkGroupId") int groupId){

        CheckGroup checkGroup = checkGroupService.findById(groupId);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(@RequestParam("checkGroupId") int groupId){
        //调用服务查询
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(groupId);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds );
    }

    /**
     * 修改提交
     * @param checkGroup 用来接收的实体类
     * @param checkitemIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        //调用业务服务
        checkGroupService.update(checkGroup,checkitemIds);
        //响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/deleteById")
    public Result deleteById(int id){

        //调用业务层
       checkGroupService.deleteById(id);
       
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }


}
