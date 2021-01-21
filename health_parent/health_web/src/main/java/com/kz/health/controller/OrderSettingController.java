package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.entity.Result;
import com.kz.health.pojo.OrderSetting;
import com.kz.health.service.OrderSettingService;
import com.kz.health.utils.POIUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-05 21:19
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入预约设置
     *
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            //解析 获取Excel中数据
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //转化
            ArrayList<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
            //声明pojo类
            OrderSetting os = null;
            //声明日期类型转换
            SimpleDateFormat dateFormat = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] arr : strings) {
                //每个数组arr表示一行记录 第一行是日期的string形式转为date类型
                Date orderDate = dateFormat.parse(arr[0]);
                int number = Integer.valueOf(arr[1]);
                os = new OrderSetting(orderDate, number);
                orderSettings.add(os);

            }
            //调用服务
            orderSettingService.add(orderSettings);
            //返回结果
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month) {
        //调用服务端
        List<Map<String, Integer>> data = orderSettingService.getOrderSettingByMonth(month);
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, data);

    }

    /**
     * 通过月份查询预约设置信息
     * 如果没有实体类接受 可以用map接收json数据
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }



}
