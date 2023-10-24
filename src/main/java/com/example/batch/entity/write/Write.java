package com.example.batch.entity.write;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.batch.config.BaseEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 写
 *
 * @author qtx
 * @since 2023-03-30
 */
@Getter
@Setter
@Builder
@Component
@AllArgsConstructor
@NoArgsConstructor
@TableName("write")
public class Write extends BaseEntity implements Serializable {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    @TableField("code1")
    private String code1;

    @TableField("code2")
    private String code2;

    @TableField("code3")
    private String code3;

    @TableField("code4")
    private String code4;

    @TableField("code5")
    private String code5;

    @TableField("code6")
    private String code6;

    @TableField("code7")
    private String code7;

    @TableField("code8")
    private String code8;

    @TableField("time")
    private LocalDateTime time;
}
