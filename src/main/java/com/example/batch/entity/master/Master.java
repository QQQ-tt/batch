package com.example.batch.entity.master;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.batch.config.BaseEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 读
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
@TableName("read")
public class Master extends BaseEntity implements Serializable {

    @TableField("name")
    private String name;

    @TableField("code")
    private String code;
}
