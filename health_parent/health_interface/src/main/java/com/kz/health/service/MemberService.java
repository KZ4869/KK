package com.kz.health.service;

import com.kz.health.pojo.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: KZ4869
 * @CreateTime: 2021-01-11 09:46
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> getMemberReport(ArrayList<String> months);
}
