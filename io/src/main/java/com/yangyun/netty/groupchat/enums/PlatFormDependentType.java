package com.yangyun.netty.groupchat.enums;

import lombok.Getter;

/**
 * @Description: 操作系统类型
 * @Author yun.Yang
 * @Date 2021/11/22 22:33
 * @Version 1.0
 **/

@Getter
public enum PlatFormDependentType {

    WIN_OS("Windows", "Windows"),
    LINUX_OS("Linux", "Linux"),
    ;

    private String osType;
    private String osName;

    PlatFormDependentType (String osType, String osName){
        this.osType = osType;
        this.osName = osName;
    }
}
