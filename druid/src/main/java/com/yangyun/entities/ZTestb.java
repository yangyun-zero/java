package com.yangyun.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ZTestb implements Serializable {
    private Long networkId;

    private String networkName;

    private Integer purchaseCount;

    @ApiModelProperty(name = "purchaseStatus", value = "")
    private Integer purchaseStatus;

    @ApiModelProperty(name = "payStatus", value = "")
    private Integer payStatus;

    @ApiModelProperty(name = "createTime", value = "")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}