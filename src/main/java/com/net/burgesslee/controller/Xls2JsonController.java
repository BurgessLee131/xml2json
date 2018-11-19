package com.net.burgesslee.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.net.burgesslee.entity.DayNightSettlementProduct;
import com.net.burgesslee.service.Xls2OracleService;

@RequestMapping("/base")
@Controller
public class Xls2JsonController {
	
	private static final String PREFIX = "/hello/";
	
//	private  static final Logger logger = LoggerFactory.getLogger(Xls2OracleServiceImpl.class);
	
	@Resource
	private Xls2OracleService service;
	
	@RequestMapping(value="/hello")
	public String hello(){
		return PREFIX +"success.html";
	}
	
	@RequestMapping("")
	public String execute(){
		return "index";
	}
	
	@RequestMapping(value="/test")
	public String getJson2Oracle(@PathVariable(value="fileName",required=false)String fileName){
//		logger.info("即将开始处理" +fileName+".xlsx文件------------------------------------------------------->");
		System.out.println("即将开始处理数据:" +fileName);
		service.xls2Oracle(fileName);
//		logger.info("处理完成" +fileName+".xlsx文件------------------------------------------------------->");
		return "success.html";
	}
	
	@RequestMapping(value="/list")
	public String getDayNightList(){
		List<DayNightSettlementProduct> dayNightList = this.service.getDayNightList();
		System.out.println(dayNightList);
		return "/list";
	}
	
}
