package com.example.batch.mapper.master;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.batch.entity.master.Master;
import com.example.batch.entity.read.Read;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 读 Mapper 接口
 * </p>
 *
 * @author qtx
 * @since 2023-03-30
 */
@Mapper
public interface MasterMapper extends BaseMapper<Master> {

    List<Master> selectRead();
}
