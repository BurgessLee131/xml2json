package com.net.burgesslee.entity;

import java.io.Serializable;

/**
 * 临时表，类似生产表
 * @author Administrator
 *
 */
public class DmOrDayNightProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unit;

	private String project;

	private String dayWork;

	private String nightWork;

	private String dayNightTotalWork;

	private String monthCumulationProduction;

	private String qoq;

	private String yoy;

	private String describe;

	private Integer sort;

	private String reportTime;

	private Integer serialNumber;

	private String progressCompletion;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getDayWork() {
		return dayWork;
	}

	public void setDayWork(String dayWork) {
		this.dayWork = dayWork;
	}

	public String getNightWork() {
		return nightWork;
	}

	public void setNightWork(String nightWork) {
		this.nightWork = nightWork;
	}

	public String getDayNightTotalWork() {
		return dayNightTotalWork;
	}

	public void setDayNightTotalWork(String dayNightTotalWork) {
		this.dayNightTotalWork = dayNightTotalWork;
	}

	public String getMonthCumulationProduction() {
		return monthCumulationProduction;
	}

	public void setMonthCumulationProduction(String monthCumulationProduction) {
		this.monthCumulationProduction = monthCumulationProduction;
	}

	public String getQoq() {
		return qoq;
	}

	public void setQoq(String qoq) {
		this.qoq = qoq;
	}

	public String getYoy() {
		return yoy;
	}

	public void setYoy(String yoy) {
		this.yoy = yoy;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getProgressCompletion() {
		return progressCompletion;
	}

	public void setProgressCompletion(String progressCompletion) {
		this.progressCompletion = progressCompletion;
	}

	@Override
	public String toString() {
		return "DmOrDayNightProduct [unit=" + unit + ", project=" + project + ", dayWork=" + dayWork + ", nightWork="
				+ nightWork + ", dayNightTotalWork=" + dayNightTotalWork + ", monthCumulationProduction="
				+ monthCumulationProduction + ", qoq=" + qoq + ", yoy=" + yoy + ", describe=" + describe + ", sort="
				+ sort + ", reportTime=" + reportTime + ", serialNumber=" + serialNumber + ", progressCompletion="
				+ progressCompletion + "]";
	}
}