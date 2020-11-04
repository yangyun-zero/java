package com.yangyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangyun.entities.ZTestb;
import com.yangyun.mapper.ZTestbMapper;
import com.yangyun.service.ZTestbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/28 15:32
 */
@Service
public class ZTestbServiceImpl extends ServiceImpl<ZTestbMapper, ZTestb> implements ZTestbService {

    @Autowired
    private ZTestbMapper zTestbMapper;

    public void testDelete (){
        LambdaQueryWrapper<ZTestb> lw = new LambdaQueryWrapper();
        zTestbMapper.testDelete();
    }

    public void testDelete2 (){
        LambdaQueryWrapper<ZTestb> lw = new LambdaQueryWrapper();
        zTestbMapper.testDelete2();
    }
}
