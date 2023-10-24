package com.example.batch.entity.write;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.batch.config.BaseEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class ListWrite extends BaseEntity implements Serializable {

    private List<Write> items = new ArrayList<>();
}
