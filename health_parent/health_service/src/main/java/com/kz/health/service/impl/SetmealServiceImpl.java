package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kz.health.dao.SetmealDao;
import com.kz.health.entity.PageResult;
import com.kz.health.entity.QueryPageBean;
import com.kz.health.exception.HealthException;
import com.kz.health.pojo.Setmeal;
import com.kz.health.service.SetmealService;
import com.kz.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2020-12-30 20:14
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //添加套餐 获取添加后的id
        setmealDao.add(setmeal);
        //通过id添加套餐和检查组的关系
        // 获取套餐的id
        Integer setmealId = setmeal.getId();
        //判断是否有选值
        if (null != checkgroupIds) {
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }

    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        //分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否有条件查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //拼接数据
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //分页查询
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());

        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    /**
     * 根据id返回相应套餐
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {

        return setmealDao.findById(id);
    }

    /**
     * 根据ID获取套餐和检查组的关系
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //填入套餐
        setmealDao.update(setmeal);
        //获取套餐id
        Integer setmealId = setmeal.getId();
        //判断是否有选值
        if (null != checkgroupIds) {
            //关系表 先删除后添加
            setmealDao.deleteSetmealCheckGroup(setmealId);
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }

    }

    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //判断与订单是否有联系 有就不能删
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count > 0) {
            throw new HealthException("该套餐已经被使用了，不能删除");
        }
        //删除时,先删除与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        //删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 查出所有的数据库中图片
     *
     * @return
     */
    @Override
    public List<String> findImgs() {

        return setmealDao.findImgs();
    }

    /**
     * 查询套餐
     *
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmealList = setmealDao.findAll();
        //拼接图片完整路径
        setmealList.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        // 生成列表静态文件
        generateSetmealList(setmealList);
        // 套餐详情页面
        generateSetmealDetals(setmealList);
        return setmealList;
    }
    /**
     * 生成 详情页面
     * @param setmealList
     */
    private void generateSetmealDetals(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            //通过id查询五表数据
            Setmeal setmealDetail= setmealDao.findDetailById(setmeal.getId());
            //完整图片路径
            setmealDetail.setImg(setmeal.getImg());
            //生成模板
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmeal", setmealDetail);
            // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码
            String templateName = "mobile_setmeal_detail.ftl";
            String filename = String.format("%s/setmeal_detail_%d.html",out_put_path,setmealDetail.getId());
            generateHtml(templateName, dataMap, filename);
        }
    }

    //引入freemarkerConfig
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private String out_put_path;

    private void generateSetmealList(List<Setmeal> setmealList) {
            //填入参数
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("setmealList", setmealList);
            // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码

            String setmealListFile = out_put_path + "/mobile_setmeal.html";
           generateHtml("mobile_setmeal.ftl",dataMap,setmealListFile);

    }

    private void generateHtml(String templateName,Map<String,Object> dataMap,String filename){
        // 获取模板
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        try {
            Template template = configuration.getTemplate(templateName);
            // 生成的文件 c:/sz89/health_parent/health_mobile/src/main/webapp/pages/m_setmeal.html
            // utf-8 不能少了。少了就中文乱码
            BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            // 填充数据到模板
            template.process(dataMap,writer);
            // 关闭流
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询套餐详情
     *
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }
    //套餐预约查询
    @Override
    public List<Map<String, Object>> findSetmealCount() {

        return setmealDao.findSetmealCount();
    }
}
