package com.example.batch.mapper.write;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.batch.entity.write.ListWrite;
import com.example.batch.entity.write.Write;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 写 Mapper 接口
 * </p>
 *
 * @author qtx
 * @since 2023-03-30
 */
@Mapper
public interface WriteMapper extends BaseMapper<Write> {

    int inertWrite(Write item);

    int inertBatchWrite(ListWrite items);

}
