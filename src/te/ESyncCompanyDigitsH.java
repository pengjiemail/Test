package te;

import java.io.Serializable;

import core.vo.SuperVO;

/**
 * @copyright Copyright (c) 2014 SouFun
 *
 * @description  主页查询数据记录（当前表）实体类
 *
 * @author chenwenju
 *
 * @since 2014-12-23 下午4:17:27
 *
 */
public class ESyncCompanyDigitsH extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 主键  */
	private Integer id;
	/** 接口对应分部 */
	private String company;
	/** 接口对应分部url */
	private String companyUrl;
	/** 今日统计 */
	private String todays;
	/** 今日url */
	private String urlTodays;
	/** 昨日统计 */
	private String yesterdays;
	/** 昨日url */
	private String urlYesterdays;
	/** 本周统计 */
	private String weeks;
	/** 本周url */
	private String urlWeeks;
	/** 本月统计 */
	private String months;
	/** 本月url */
	private String urlMonths;
	/** 本年统计 */
	private String years;
	/** 本年url */
	private String urlYears;
	/** 接口系统统计的时间 */
	private String interfaceDate;
	/** 创建时间（OA系统调用接口时间） */
	private String createDate;
	/** 类别 */
	private String category;
	/** 名称 */
	private String name;
	/** 预算 */
	private String budget;
	/**去年本月*/
	private String lastyearmonth;

	@Override
	public String getPKFieldName() {
		return "id";
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {
		return "Sync_company_digits_h";
	}

	@Override
	public String getSequence() {
		return "Sync_company_digits_h_id";
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the companyUrl
	 */
	public String getCompanyUrl() {
		return companyUrl;
	}

	/**
	 * @param companyUrl the companyUrl to set
	 */
	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	/**
	 * @return the todays
	 */
	public String getTodays() {
		return todays;
	}

	/**
	 * @param todays the todays to set
	 */
	public void setTodays(String todays) {
		this.todays = todays;
	}

	/**
	 * @return the urlTodays
	 */
	public String getUrlTodays() {
		return urlTodays;
	}

	/**
	 * @param urlTodays the urlTodays to set
	 */
	public void setUrlTodays(String urlTodays) {
		this.urlTodays = urlTodays;
	}

	/**
	 * @return the yesterdays
	 */
	public String getYesterdays() {
		return yesterdays;
	}

	/**
	 * @param yesterdays the yesterdays to set
	 */
	public void setYesterdays(String yesterdays) {
		this.yesterdays = yesterdays;
	}

	/**
	 * @return the urlYesterdays
	 */
	public String getUrlYesterdays() {
		return urlYesterdays;
	}

	/**
	 * @param urlYesterdays the urlYesterdays to set
	 */
	public void setUrlYesterdays(String urlYesterdays) {
		this.urlYesterdays = urlYesterdays;
	}

	/**
	 * @return the weeks
	 */
	public String getWeeks() {
		return weeks;
	}

	/**
	 * @param weeks the weeks to set
	 */
	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}

	/**
	 * @return the urlWeeks
	 */
	public String getUrlWeeks() {
		return urlWeeks;
	}

	/**
	 * @param urlWeeks the urlWeeks to set
	 */
	public void setUrlWeeks(String urlWeeks) {
		this.urlWeeks = urlWeeks;
	}

	/**
	 * @return the months
	 */
	public String getMonths() {
		return months;
	}

	/**
	 * @param months the months to set
	 */
	public void setMonths(String months) {
		this.months = months;
	}

	/**
	 * @return the urlMonths
	 */
	public String getUrlMonths() {
		return urlMonths;
	}

	/**
	 * @param urlMonths the urlMonths to set
	 */
	public void setUrlMonths(String urlMonths) {
		this.urlMonths = urlMonths;
	}

	/**
	 * @return the years
	 */
	public String getYears() {
		return years;
	}

	/**
	 * @param years the years to set
	 */
	public void setYears(String years) {
		this.years = years;
	}

	/**
	 * @return the urlYears
	 */
	public String getUrlYears() {
		return urlYears;
	}

	/**
	 * @param urlYears the urlYears to set
	 */
	public void setUrlYears(String urlYears) {
		this.urlYears = urlYears;
	}

	/**
	 * @return the interfaceDate
	 */
	public String getInterfaceDate() {
		return interfaceDate;
	}

	/**
	 * @param interfaceDate the interfaceDate to set
	 */
	public void setInterfaceDate(String interfaceDate) {
		this.interfaceDate = interfaceDate;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getLastyearmonth() {
		return lastyearmonth;
	}

	public void setLastyearmonth(String lastyearmonth) {
		this.lastyearmonth = lastyearmonth;
	}
	
}
