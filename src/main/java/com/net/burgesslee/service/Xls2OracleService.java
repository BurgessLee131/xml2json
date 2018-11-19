package com.net.burgesslee.service;

import java.util.List;

import com.net.burgesslee.entity.DayNightSettlementProduct;

public interface Xls2OracleService {
	
	/**
	 * 读取数据并将数据插入到oracle
	 * @return
	 */
	public void xls2Oracle(String fileName);
	
	/**
	 * 查询数据库中保存的所有的数据的sort和serialNumber的数值
	 * @return
	 */
	public List<DayNightSettlementProduct> getDayNightList();
	
}
