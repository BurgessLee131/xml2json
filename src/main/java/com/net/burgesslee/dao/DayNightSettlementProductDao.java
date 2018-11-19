package com.net.burgesslee.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.burgesslee.entity.DayNightSettlementProduct;

/**
 * @author Administrator
 */
@Mapper
public interface DayNightSettlementProductDao {
	
	//插入数据
    int insert(DayNightSettlementProduct record);
    //选择性插入数据
    int insertSelective(DayNightSettlementProduct record);
    //根据指定条件查询对应的数据
	List<DayNightSettlementProduct> selectByFilter(Map<String, Object> filter);
	/**
	 * 获取对应的sort和serialNumber
	 * @return
	 */
	List<DayNightSettlementProduct> selectList();
    
}