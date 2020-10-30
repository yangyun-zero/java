package com.yangyun.controller;

import com.yangyun.service.ZTestbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangyun
 * @Description:
 * @date 2020/10/29 16:22
 */
@RequestMapping("/zTestb")
@RestController
public class ZTesetbController {

    @Autowired
    private ZTestbService zTestbService;

    @PostMapping("/testDelete")
    public void testDelete (){
        zTestbService.testDelete();
    }
}
