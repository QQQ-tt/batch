package com.example.batch.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qtx
 * @since 2022/8/30
 */
@Data
public class BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createOn;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateOn;

    @JsonIgnore
    private Integer deleteFlag;
}
