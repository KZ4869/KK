package com.kz.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kz.health.dao.MemberDao;
import com.kz.health.pojo.Member;
import com.kz.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-12 08:37
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {


    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        Member byTelephone = memberDao.findByTelephone(telephone);
        return byTelephone;
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 根据会员表
     * 每个月最后一天会员总数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(ArrayList<String> months) {
        //select count(1) from t_member where regTime<='2020-06-31';  <= Before
        //遍历日期
        List<Integer> memberCount = new ArrayList<Integer>();
        if(months != null) {
            for (String month : months) {
                month += month+"-31";
            //循环12个月 每月查询一下
                Integer count = memberDao.findMemberCountBeforeDate(month);
                memberCount.add(count);

            }
        }

        return memberCount;
    }
}
