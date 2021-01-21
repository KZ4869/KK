package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.entity.Result;
import com.kz.health.pojo.Setmeal;
import com.kz.health.service.SetmealService;
import com.kz.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-30 18:07
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    /**
     * 上传套餐图片
     *
     * @param imgFile
     * @return
     */
    @RequestMapping("upload")
    public Result upload(MultipartFile imgFile) {
        //获取原有的文件,并截取后缀名
        String originalFilename = imgFile.getOriginalFilename();
        //获取图片后缀名'.'开始的下标
        int indexOf = originalFilename.indexOf(".");
        //获取后缀名
        String extension = originalFilename.substring(indexOf);
        //添加新的唯一的文件名
        String fileName = UUID.randomUUID() + extension;

        //调用七牛云上传文件

        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), fileName);
            //- 返回数据给页面
            //{
            //    flag:
            //    message:
            //    data:{
            //        imgName: 图片名,
            //        domain: QiNiuUtils.DOMAIN
            //    }
            //}
            //没有pojo对象 就用map封装
            Map<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("imgName", fileName);
            hashMap.put("domain", QiNiuUtils.DOMAIN);
            //成功后反馈给前端
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, hashMap);


        } catch (IOException e) {
            e.printStackTrace();
        }
        //失败也反馈前端
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);

    }

    /**
     * 添加套餐
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        setmealService.add(setmeal, checkgroupIds);
        //响应结果
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查找
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    /**
     * 通过id查询套餐
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        Setmeal setmeal = setmealService.findById(id);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("setmeal",setmeal);
        map.put("domain", QiNiuUtils.DOMAIN);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, map);
    }
    //根据套餐ID获取有哪些相应的检查组
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> ids = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, ids);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        setmealService.update(setmeal, checkgroupIds);
        //响应结果
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 通过id删除套餐
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
