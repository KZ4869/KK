package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.Result;
import com.kz.health.pojo.Setmeal;
import com.kz.health.service.SetmealService;

import com.kz.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 套餐列表
     * @return
     */
    @GetMapping("/getSetmeal")
    public Result findAll(){
        // 查询所有
        List<Setmeal> setmealList = setmealService.findAll();
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmealList);
    }

    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        //调用服务查询套餐详情
        Setmeal setmeal =setmealService.findDetailById(id);
        //拼接完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);

    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
    //套餐信息
        Setmeal setmeal = setmealService.findById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }


}
