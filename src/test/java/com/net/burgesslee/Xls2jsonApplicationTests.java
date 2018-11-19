package com.net.burgesslee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.net.burgesslee.dao.DmOrDayNightProductDao;
import com.net.burgesslee.entity.DmOrDayNightProduct;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(basePackages={"com.net.burgesslee.dao"})
public class Xls2jsonApplicationTests {
	
	@Autowired
	private DmOrDayNightProductDao dayNightProductDao;	
	
	@Test
	public void contextLoads() {
		DmOrDayNightProduct dmOrDayNightProduct = new DmOrDayNightProduct();
		dmOrDayNightProduct.setUnit("test");
		dayNightProductDao.insertSelective(dmOrDayNightProduct);
	}

}
