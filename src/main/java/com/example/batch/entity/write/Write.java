package com.example.batch.entity.write;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.batch.config.BaseEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

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
}
