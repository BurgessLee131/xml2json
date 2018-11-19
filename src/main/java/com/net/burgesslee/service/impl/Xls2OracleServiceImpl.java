package com.net.burgesslee.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.net.burgesslee.dao.DayNightSettlementProductDao;
import com.net.burgesslee.dao.DmOrDayNightProductDao;
import com.net.burgesslee.entity.DayNightSettlementProduct;
import com.net.burgesslee.entity.DmOrDayNightProduct;
import com.net.burgesslee.service.Xls2OracleService;
import com.net.burgesslee.utils.ExcelUtils;
import com.net.burgesslee.utils.FileFilter;
import com.net.burgesslee.utils.FileUtils;
import com.net.burgesslee.utils.MapUtils;
import com.net.burgesslee.utils.StringTools;

@Service
public class Xls2OracleServiceImpl implements Xls2OracleService {
	
//	private  static final Logger logger = LoggerFactory.getLogger(Xls2OracleServiceImpl.class);
	 
	@Autowired
	private DayNightSettlementProductDao dayNightSettlementProductDao;
	
	@Autowired
	private DmOrDayNightProductDao dmOrDayNightProductDao;
	
	public static String reportTime = "";

	@Override
	public void xls2Oracle(String fileName) {
		try {
			//两个路径下的文件
			//添加对应的文件路径名称
			List<String> files = Arrays.asList();
			for(String path : files){
				System.out.println("将要处理的路径是：----------------" +path+ "-------------------------");
				
				//针对每个路径下的文件进行处理
				reportTime = "";
				List<String> fnNeededHandle = FileUtils.getAllFileName(path);
				Stream.of(fnNeededHandle).forEach(System.out::println);
				for(String fn : fnNeededHandle){
					System.out.println("将要处理的文件是：----------------" +fn+ "-------------------------");
					//真正要被处理的文件的真实路径
//					String realPath = path + "\\"+ fn;
//					System.out.println("realPath=" + realPath);
					
					//TODO 文件的过滤
					if(FileFilter.FILTER.contains(fn)){
						continue;
					}
					
//					logger.info("将要处理的文件是：----------------" +path+ "-------------------------");
			        //读取excel数据
			        List<Map<String,String>> result = ExcelUtils.readExcelToObj(fn);
			        /*for (Map<String, String> map : result) {
						Stream.of(map).forEach(System.out::println);
					}*/
			        //解析处理成json
			        String json = new Gson().toJson(result);
			        System.out.println("处理成的json数据是 " +json);
//			        logger.info("处理成的json数据是 " +json);
					System.out.println("文件处理完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//					logger.info("文件处理完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					
					/*
					List<DayNightSettlementProduct> selectList = this.dayNightSettlementProductDao.selectList();
					Map<String,String> sortAndSerialNumber = new HashMap<>();
					if(selectList != null && selectList.size() > 0){
						for(DayNightSettlementProduct d : selectList){
							sortAndSerialNumber.put(d.getUnit().trim()+"-"+d.getProject().trim(), d.getSort()+"-"+d.getSerialNumber());
						}
					}else{
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!--未查到相应记录-------------------");
					}
					*/
//					System.out.println("map=" +sortAndSerialNumber);
					
					//进行插入数据
					for(Map<String,String> map : result){
						//跳过执行逻辑
						/**
						if(1 == 1){
							continue;
						}
						*/
						//过滤无用数据
					    if(map == null){
					    	continue;
					    }
					    /*
					    else if(map.get("同比2")!= null && map.get("同比2").contains("填报人") && index < 2){
					    	reportTime = map.get("合计2");
							continue;
						}
						*/
					    else if(map.get("单位") == null || "".equals(map.get("单位")) || map.get("单位2") == null || "".equals(map.get("单位2"))){
							continue;
						//过滤无用数据
						}else if("0".equals(map.get("单位")) || "0".equals(map.get("单位2")) || "单位".equals(map.get("单位2")) || "单位".equals(map.get("单位"))){
							continue;
						}else{//插入数据
//							System.out.println("0000000000000000" +selectList.size());
//							Stream.of(selectList).forEach(System.out::println);
							//对数据进行处理封装
//							DayNightSettlementProduct record = handlerEntityMethod(map,realPath,MapUtils.MAP);

							DmOrDayNightProduct record = handlerEntityMethod(map,fn,MapUtils.MAP, reportTime);							
//							logger.info("插入的第一条数据是" + record+"================================================");
							System.out.println("插入的第二条数据是" + record+"================================================");
							dmOrDayNightProductDao.insertSelective(record);
						}
					}
//					logger.info("excel表格处理完成");
				}
			}
			
		} catch (Exception e) {
//			logger.info("发生异常信息" + e.getMessage());
			System.out.println("发生异常信息了.............");
			e.printStackTrace();
		}
		
		return;
	}

	/**
	 * 处理解析的数据
	 * @param map  待处理的解析的数据
	 * @param fileName 文件名称  如2017.2.1
	 * @return
	 */
	@SuppressWarnings("unused")
	private DmOrDayNightProduct handlerEntityMethod(Map<String, String> map,String fileName,Map<String,String> sortAndSerialNumber,String reportTime) {
		//创建一个具体对应的实体类
		DmOrDayNightProduct dmOrDayNightProduct = new DmOrDayNightProduct();
		//处理过程
		String unitOne    = map.get("单位");//第一个单位
		String unitTwo    = map.get("单位2");//第二个单位
		String projectOne = map.get("作业项目");//第一个作业项目
		String projectTwo = map.get("作业项目2");//第二个作业项目
		String outputOne  = map.get("生产量");//第一个生产量
		String outputTwo  = map.get("生产量2");//第二个生产量
		
		String dayOne   = map.get("白班");//第一个白班
		String dayTwo   = map.get("白班2");//第二个白班2
		String nightOne = map.get("夜班");//第一个夜班
		String nightTwo = map.get("夜班2");//第二个夜班2
		String totalOne = map.get("合计");//第一个合计
		String totalTwo = map.get("合计2");//第二个合计2
		
		String qoqOne = map.get("同比");//第一个同比
		String qoqTwo = map.get("同比2");//第二个同比2
		String yoyOne = map.get("环比");//第一个环比
		String yoyTwo = map.get("环比2");//第二个环比2
		
		//获取对应的sort和serialNumber字段的值
		/*Map<String,Object> filter = new HashMap<>();
		filter.put("unit", unitOne);
		filter.put("project", projectOne);
		List<DayNightSettlementProduct> filterResult = this.dayNightSettlementProductDao.selectByFilter(filter);
		*/
		//唯一标识
		//Integer sort = 0;
		//序列号
		//Integer serialNumber = 0;
		//报告生成时间
		
		if(outputOne != null && outputOne.contains("/")){
//			String[] project = projectOne.split("/");
			String key = unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]")+"-"+projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]");
			String str = sortAndSerialNumber.get(key);
			if(str == null || "".equals(str)){
				str = "999999-999999";
			}
			String[] sortAndNumber = str.split("-");
			System.out.println(unitOne+"-"+projectOne +"-----------------" +sortAndNumber[0]+"-"+sortAndNumber[1]);
			
			if(outputOne == null){
				dmOrDayNightProduct.setMonthCumulationProduction(String.valueOf(0));
			}else if(StringTools.checkcountname(outputOne)){//true 存在汉字
				dmOrDayNightProduct.setMonthCumulationProduction(outputOne);
			}else{
				dmOrDayNightProduct.setMonthCumulationProduction(outputOne.split("/")[0]);
			}
			dmOrDayNightProduct.setUnit(unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayOne);
			dmOrDayNightProduct.setNightWork(nightOne);
			dmOrDayNightProduct.setDayNightTotalWork(totalOne);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber[1]));
			dmOrDayNightProduct.setQoq(qoqOne);
			dmOrDayNightProduct.setYoy(yoyOne);
			dmOrDayNightProduct.setReportTime(reportTime);
			
//		logger.info("插入的第一条数据是" + dayNightSettlementProduct+"================================================");
			System.out.println("插入的第一条数据是" + dmOrDayNightProduct+"================================================");
			//完成第一条记录插入
			this.dmOrDayNightProductDao.insertSelective(dmOrDayNightProduct);
			
			String key1 = unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]")+"-"+projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]");
			String str1 = sortAndSerialNumber.get(key1);
			if(str1 == null || "".equals(str1)){
				str1 = "999999-999999";
			}
			String[] sortAndNumber1 = str1.split("-");
			System.out.println(unitOne+"-"+projectOne +"-----------------" +sortAndNumber1[0]+"-"+sortAndNumber1[1]);
			
			if(outputOne == null){
				dmOrDayNightProduct.setMonthCumulationProduction(String.valueOf(0));
			}else if(StringTools.checkcountname(outputOne)){//true 存在汉字
				dmOrDayNightProduct.setMonthCumulationProduction(outputOne);
			}else{
				dmOrDayNightProduct.setMonthCumulationProduction(outputOne.split("/")[1]);
			}
			
			dmOrDayNightProduct.setUnit(unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayOne);
			dmOrDayNightProduct.setNightWork(nightOne);
			dmOrDayNightProduct.setDayNightTotalWork(totalOne);
			dmOrDayNightProduct.setQoq(qoqOne);
			dmOrDayNightProduct.setYoy(yoyOne);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber1[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber1[1]));
			dmOrDayNightProduct.setReportTime(reportTime);
//		logger.info("插入的第一条数据是" + dayNightSettlementProduct+"================================================");
			System.out.println("插入的第一条数据是" + dmOrDayNightProduct+"================================================");
			//完成第一条记录插入
			this.dmOrDayNightProductDao.insertSelective(dmOrDayNightProduct);
			
		}else{
			String key = unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]")+"-"+projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]");
			String str = sortAndSerialNumber.get(key);
			if(str == null || "".equals(str)){
				str = "999999-999999";
			}
			String[] sortAndNumber = str.split("-");
			
			dmOrDayNightProduct.setUnit(unitOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectOne.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayOne);
			dmOrDayNightProduct.setNightWork(nightOne);
			dmOrDayNightProduct.setDayNightTotalWork(totalOne);
			dmOrDayNightProduct.setQoq(qoqOne);
			dmOrDayNightProduct.setYoy(yoyOne);
			dmOrDayNightProduct.setMonthCumulationProduction(outputOne);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber[1]));
			dmOrDayNightProduct.setReportTime(reportTime);
//		logger.info("插入的第一条数据是" + dayNightSettlementProduct+"================================================");
			System.out.println("插入的第一条数据是" + dmOrDayNightProduct+"================================================");
			//完成第一条记录插入
			this.dmOrDayNightProductDao.insertSelective(dmOrDayNightProduct);
		}
		
		
		//清空对应的filter中的数据
		/*
		filter.clear();
		filter.put("unit", unitTwo);
		filter.put("project", projectTwo);
		List<DayNightSettlementProduct> anotherFilterResult = this.dayNightSettlementProductDao.selectByFilter(filter);
		if(anotherFilterResult != null && anotherFilterResult.size() > 0){
			for(DayNightSettlementProduct d :anotherFilterResult){
				if(d.getSort() != null || d.getSerialNumber() != null){
					sort = d.getSort();
					serialNumber = d.getSerialNumber();
					System.out.println(d +"111111111111111");
					break;
				}
			}
		}else{
			System.out.println("未查询到结果");
		}
		*/
		
		if(unitTwo != null && unitTwo.contains("/")){
//			String[] project = projectTwo.split("/");
			//获取对应的sort和number
			String str2 = sortAndSerialNumber.get(unitTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]")+"-"+projectTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			if(str2 == null || "".equals(str2)){
				str2 = "999999-999999";
			}
			String[] sortAndNumber2 = str2.split("-");
			System.out.println(unitTwo+"-"+projectTwo +"-----------------" +sortAndNumber2[0]+"-"+sortAndNumber2[1]);
			
			if(outputTwo == null){
				dmOrDayNightProduct.setMonthCumulationProduction(String.valueOf(0));
			}else if(StringTools.checkcountname(outputTwo)){//true 存在汉字
				dmOrDayNightProduct.setMonthCumulationProduction(outputTwo.trim());
			}else{
				dmOrDayNightProduct.setMonthCumulationProduction(outputTwo.split("/")[0]);
			}
			
			//封装第二条数据的记录
			dmOrDayNightProduct.setUnit(unitTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayTwo);
			dmOrDayNightProduct.setNightWork(nightTwo);
			dmOrDayNightProduct.setDayNightTotalWork(totalTwo);
			dmOrDayNightProduct.setQoq(qoqTwo);
			dmOrDayNightProduct.setYoy(yoyTwo);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber2[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber2[1]));
			dmOrDayNightProduct.setReportTime(reportTime);
			
			//获取对应的sort和number
			String str = sortAndSerialNumber.get(unitTwo.trim()+"-"+projectTwo.trim());
			if(str == null || "".equals(str)){
				str = "999999-999999";
			}
			String[] sortAndNumber = str.split("-");
			System.out.println(unitTwo+"-"+projectTwo +"-----------------" +sortAndNumber[0]+"-"+sortAndNumber[1]);
			
			if(outputTwo == null){
				dmOrDayNightProduct.setMonthCumulationProduction(String.valueOf(0));
			}else if(StringTools.checkcountname(outputTwo)){//true 存在汉字
				dmOrDayNightProduct.setMonthCumulationProduction(outputTwo);
			}else{
				System.out.println("outputTwo=" +outputTwo);
				dmOrDayNightProduct.setMonthCumulationProduction(outputTwo.split("/")[1]);
			}
			
			//封装第二条数据的记录
			dmOrDayNightProduct.setUnit(unitTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayTwo);
			dmOrDayNightProduct.setNightWork(nightTwo);
			dmOrDayNightProduct.setDayNightTotalWork(totalTwo);
			dmOrDayNightProduct.setQoq(qoqTwo);
			dmOrDayNightProduct.setYoy(yoyTwo);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber[1]));
			dmOrDayNightProduct.setReportTime(reportTime);
			return dmOrDayNightProduct;
		}else{
			//获取对应的sort和number
			String str2 = sortAndSerialNumber.get(unitTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]")+"-"+projectTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			if(str2 == null || "".equals(str2)){
				str2 = "999999-999999";
			}
			String[] sortAndNumber2 = str2.split("-");
			System.out.println(unitTwo+"-"+projectTwo +"-----------------" +sortAndNumber2[0]+"-"+sortAndNumber2[1]);
			//封装第二条数据的记录
			dmOrDayNightProduct.setUnit(unitTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setProject(projectTwo.trim().replaceAll("[(]", "[（]").replaceAll("[(]", "[）]"));
			dmOrDayNightProduct.setDayWork(dayTwo);
			dmOrDayNightProduct.setNightWork(nightTwo);
			dmOrDayNightProduct.setDayNightTotalWork(totalTwo);
			dmOrDayNightProduct.setQoq(qoqTwo);
			dmOrDayNightProduct.setYoy(yoyTwo);
			dmOrDayNightProduct.setMonthCumulationProduction(outputTwo);
			dmOrDayNightProduct.setSort(Integer.parseInt(sortAndNumber2[0]));
			dmOrDayNightProduct.setSerialNumber(Integer.parseInt(sortAndNumber2[1]));
			dmOrDayNightProduct.setReportTime(reportTime);
			return dmOrDayNightProduct;
		}
		
	}

	@Override
	public List<DayNightSettlementProduct> getDayNightList() {
		return dayNightSettlementProductDao.selectList();
	}

}
