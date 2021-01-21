package com.kz.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kz.health.constant.MessageConstant;
import com.kz.health.constant.RedisMessageConstant;
import com.kz.health.entity.Result;
import com.kz.health.pojo.CheckGroup;
import com.kz.health.pojo.CheckItem;
import com.kz.health.pojo.Order;
import com.kz.health.pojo.Setmeal;
import com.kz.health.service.OrderService;
import com.kz.health.service.SetmealService;
import com.kz.health.utils.DateUtils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-10 16:00
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 获取redis中的连接池
     */
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    /**
     * 提交 预约
     *
     * @param paraMap
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> paraMap) {
        //验证码校验
        Jedis jedis = jedisPool.getResource();
        //获取手机号
        String telephone = paraMap.get("telephone");
        //拼接验证码的key
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        //redis中的验证码
        String codeInRedis = jedis.get(key);
        //判断是否为空
        if (StringUtils.isEmpty(codeInRedis)) {
            //没有
            return new Result(false, "请重新获取验证码!");
        }
        //前端传递验证码 是否一致
        String validateCode = paraMap.get("validateCode");
        if (!codeInRedis.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //移除redis中的验证码 防止重复提交
        //jedis.del(key); 实际开发需要
        jedis.close();
        //设置预约设置类型
        paraMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        // 预约成功页面展示时需要用到id
        Order order = orderService.submitOrder(paraMap);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }

    @GetMapping("/findById")
    public Result findById(int id) {
        Map<String, Object> orderInfo = orderService.findOrderDetailById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, orderInfo);
    }

    /**
     * 导出pdf
     *
     * @param id
     * @param res
     */
    @RequestMapping("/exportSetmealInfo")
    public Result exportSetmealInfo(int id, HttpServletResponse res) {
        //查询预约成功信息
        Map<String, Object> orderDetail = orderService.findOrderDetailById(id);
        //查询套餐详情
        Setmeal setmeal = setmealService.findDetailById((int) orderDetail.get("setmeal_id"));
        //创建文档
        Document document = new Document();
        //设置文件下载头信息
        res.setHeader("Content-Disposition","attachment;filename=setmealInfo.pdf");
        //响应的内容格式
        res.setContentType("application/pdf");
        // 创建writer, outputstream res.getOutputStream
        try {
            PdfWriter.getInstance(document, res.getOutputStream());
            //打开文档
            document.open();
            //宋体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //&#x5185;&#x5bb9;&#x4f53;
            Font chinese = new Font(bfChinese);
            //表头的字体
            Font headerFont = new Font(bfChinese);
            // 添加内容
            // 预约成功信息 paragraph
            // 预约日期格式处理
            String orderDate = DateUtils.parseDate2String((Date) orderDetail.get("orderDate"));
            document.add(new Paragraph("体检人：" + (String) orderDetail.get("member"),chinese));
            document.add(new Paragraph("体检套餐：" + (String) orderDetail.get("setmeal"),chinese));
            document.add(new Paragraph("体体检日期：" + orderDate,chinese));
            document.add(new Paragraph("预约类型：" + (String) orderDetail.get("orderType"),chinese));
            //    套餐详情 table, 参数1：多少列
            Table table = new Table(3);
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式
            table.addCell(buildCell("项目名称",headerFont));
            table.addCell(buildCell("项目内容",headerFont));
            table.addCell(buildCell("项目解读",headerFont));
            // 内容体
            List<CheckGroup> checkGroups = setmeal.getCheckGroups();
            if(null != checkGroups){
                // 检查组
                for (CheckGroup checkGroup : checkGroups) {
                    // 检查组名称
                    table.addCell(buildCell(checkGroup.getName(),chinese));

                    // 检查项
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    StringBuilder sb = new StringBuilder();
                    if(null != checkItems){
                        for (CheckItem checkItem : checkItems) {
                            sb.append(checkItem.getName()).append(" ");
                        }
                        sb.setLength(sb.length()-1); // 去掉最后一个空格
                    }

                    table.addCell(buildCell(sb.toString(),chinese));

                    // 检查组的项目解读
                    table.addCell(buildCell(checkGroup.getRemark(),chinese));
                }
            }
            // 文档要添加表格
            document.add(table);
            // 关闭文档
            document.close();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "导出失败");
        }
    }

    // 传递内容和字体样式，生成单元格
    private Cell buildCell(String content, Font font)
            throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
