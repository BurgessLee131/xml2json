package com.net.burgesslee.dao;

import org.apache.ibatis.annotations.Mapper;

import com.net.burgesslee.entity.DmOrDayNightProduct;

@Mapper
public interface DmOrDayNightProductDao {
	
    public int insert(DmOrDayNightProduct record);
    public int insertSelective(DmOrDayNightProduct record);
}