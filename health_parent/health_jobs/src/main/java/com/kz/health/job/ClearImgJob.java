package com.kz.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.service.SetmealService;
import com.kz.health.utils.QiNiuUtils;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-01 13:01
 */
@Component
public class ClearImgJob {
    //订阅服务
    @Reference
    private SetmealService setmealService;

    /**
     * 定时销毁无用图片
     */

    public void cleanImg() {
        //查出7牛上的所有图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        //查出数据库中的所有图片
        List<String> imgInDb = setmealService.findImgs();
        //移除和数据库中一样的数据 剩下的就是垃圾数据
        imgIn7Niu.removeAll(imgInDb);
        //删除垃圾数据
        String[] strings = imgIn7Niu.toArray(new String[]{});
        QiNiuUtils.removeFiles(strings);


    }
}
