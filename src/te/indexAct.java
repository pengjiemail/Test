package te;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import work.entity.hrm.EHrmpwdLog;
import work.entity.request.EWfReqCurrentoperator;
import work.entity.sync.ESyncCompanyDigitsH;
import work.entity.sys.ESysAdvertisement;
import work.entity.sys.ESysUrgentNotice;
import work.entity.sys.ESyscookies;
import work.entity.sys.SessionUser;
import work.entity.sys.docs.ESysDocs;
import work.entity.workflow.EWfWorkflowType;
import work.service.hrm.HrmResourceMng;
import work.service.hrm.HrmReviewQuestionsMng;
import work.service.hrm.HrmpwdLogMng;
import work.service.index.IndexMng;
import work.service.request.WfReqCurrentoperatorMng;
import work.service.sync.SyncCompanyDigitsHMng;
import work.service.sync.SyncCompanyRelateHMng;
import work.service.sys.SysAdvertisementMng;
import work.service.sys.SysCooKiesMng;
import work.service.sys.SysFileTypeMng;
import work.service.sys.SysUrgentNoticeMng;
import work.service.sys.docs.SysDocsMng;
import work.service.workflow.WfCustomListMng;
import work.service.workflow.WfWorkflowParamMng;
import work.service.workflow.WfWorkflowTypeMng;
import work.webservice.admwebservice.IWSAssetBase;
import core.action.BaseAct;
import core.cache.MemClient;
import core.oaconst.SysParamConst;
import core.oaconst.WfWorkflowParamConst;
import core.util.BeanFactory;
import core.util.DateUtil;
import core.util.JsonUtil;
import core.util.MemKeyUtil;
import core.util.ResultInfo;
import core.util.Util;
import core.vo.PagerVO;

@Controller
@RequestMapping("/indexAct.do")
public class indexAct extends BaseAct{

	@Autowired
	private HrmpwdLogMng hrmPwdLogMng;
	@Autowired
	private IndexMng indexMng;
	@Autowired
	private SysAdvertisementMng sysAdvertisementMng;
	@Autowired
	private SysFileTypeMng sysFileTypeMng;
	@Autowired
	private SysDocsMng sysDocsMng;
	@Autowired
	private WfReqCurrentoperatorMng wfReqCurrentoperatorMng;
	@Autowired
	private HrmResourceMng hrmResourceMng;
	@Autowired
	private HrmReviewQuestionsMng hrmReviewQuestionsMng;
	@Autowired
	private WfCustomListMng wfCustomListMng;
	@Autowired
	private WfWorkflowTypeMng workflowTypeMng;
	@Autowired
	private WfWorkflowParamMng wfWorkflowParamMng;
	@Autowired
	private SyncCompanyDigitsHMng syncCompanyDigitsHMng;
	@Autowired
	private SyncCompanyRelateHMng syncCompanyRelateHMng;
	@Autowired
	private SysUrgentNoticeMng sysUrgentNoticeMng;
	@Autowired
	private SysCooKiesMng sysCooKiesMng;
	
	/*	bdp解析ftl测试*/
	@RequestMapping(params="method=parseFtl")
	public String parseFtl(HttpServletRequest request, ModelMap model) throws Exception{
		SessionUser user = getUserFromSession(request);
		String formHtml="";
		loadCompanyDigits(request,model,user);
		Map<String, Object> OAConst = new HashMap<String, Object>();
		OAConst.put("JSPATH", "");
		OAConst.put("JSCOUNT", "");
		model.put("OAConst", OAConst);
		formHtml = Util.loadTemplate("part0.ftl", "index", "GBK", model);
		//formHtml = Util.loadTemplate("274_244.ftl", "WEB-INF/templates/mobile", "GBK", model);
		/*PrintWriter out= new PrintWriter(System.out);
		out.print(formHtml);*/

		return "/mobile/request/test.html";
		
	}
	
	/**
	 * 跳转到主页
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=toIndexPage")
	public String toIndexPage(HttpServletRequest request, ModelMap model) throws Exception{
		//TODO 权限
		SessionUser user = getUserFromSession(request);
		//待办事宜
		getWaitEventNum(request,model,user);
		//快速通道
		getExpressWay(request,model,user);
		//最近审批
		getLastRequest(request,model,user);
		// 系统紧急通知
		List<ESysUrgentNotice> urgentNotices = sysUrgentNoticeMng.getIsvalidList();
		if (null != urgentNotices && urgentNotices.size() > 0) {
			model.put("urgentNotice", urgentNotices.get(0));
		}
		boolean subordinateRight =this.rightAuth(request, "hrm:subordinateView");
		model.put("subordinateRight", subordinateRight);
		//加载搜房集团数字统计
		loadCompanyDigits(request,model,user);
		//当前年份
		model.put("nowyear", DateUtil.getCurrentDateStr(DateUtil.C_YYYY));
		//密码
		EHrmpwdLog pwdLog = new EHrmpwdLog();
		pwdLog.setResourceid(user.getId());
		List<EHrmpwdLog> dellList = this.hrmPwdLogMng.findByList(pwdLog," id desc ");
		if(dellList!=null&&dellList.size()>0){
			EHrmpwdLog pwd = dellList.get(0);
			model.put("operatedate", pwd.getOperatedate());
		}else{
			model.put("operatedate", "");
		}
		
		return "/index/index";
	}
	/**
	 * 跳转到高管门户
	 * @decription 
	 * @author shangwenyu
	 * @time 2015-4-10 上午10:46:05
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params="method=toExecutivesPage")
	public String toExecutivesPage(HttpServletRequest request, ModelMap model) throws Exception{
		//TODO 权限  未登录提示
		SessionUser user = this.getUserFromSession(request);
		if (!this.validRight(user)) {
			model.addAttribute("rightauth_msg", "您不能查看高管门户");
			return "/msg/noright";
		}
		super.initPageCom(request, 6);
		//加载搜房集团数字统计
		loadCompanyDigits(request,model,user);
		//当前年份
		model.put("nowyear", DateUtil.getCurrentDateStr(DateUtil.C_YYYY));
		
		Integer isremark = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
		// 查询紧急
		List<EWfReqCurrentoperator> eWfWorkflowTypewaitjinjiList = this.wfReqCurrentoperatorMng.getCurReqListByOperateId(user.getId(), isremark, null, null, WfWorkflowParamConst.ISNO," and wb.levels=2 ");
		// 查询重要
		List<EWfReqCurrentoperator> eWfWorkflowTypewaitzhongyaoList = this.wfReqCurrentoperatorMng.getCurReqListByOperateId(user.getId(), isremark, null, null, WfWorkflowParamConst.ISNO," and wb.levels=1 ");
		// 查询一般
		List<EWfReqCurrentoperator> eWfWorkflowTypewaityibanList = this.wfReqCurrentoperatorMng.getCurReqListByOperateId(user.getId(), isremark, null, null, WfWorkflowParamConst.ISNO," and wb.levels=0 ");
		model.put("eWfWorkflowTypewaitjinjiList", eWfWorkflowTypewaitjinjiList);
		model.put("eWfWorkflowTypewaitzhongyaoList", eWfWorkflowTypewaitzhongyaoList);
		model.put("eWfWorkflowTypewaityibanList", eWfWorkflowTypewaityibanList);
		model.put("user", user);
		return "/index/executivesPageIndex";
	}
	
	
	/**
     * 今日新增
     * @param request
     * @param response
     * @param model
     * @return    
     * @author zhangjiwei
     * @throws Exception 
     * @date 2015-4-26 02:36:33 
     *
     */
    @RequestMapping(params = "method=getOfficeToday")
    public String getOfficeToday(HttpServletRequest request,HttpServletResponse response, ModelMap model,Integer pager_offset,Integer pagesize,Integer flag) throws Exception{
    	model.addAttribute("flag", flag);
		return "/index/officeToday";
    }
    @RequestMapping(params = "method=dealOfficeOfWaitApp")
    @ResponseBody
    public String dealOfficeOfWaitApp(HttpServletRequest request,HttpServletResponse response, ModelMap model,Integer pager_offset,Integer pagesize,Integer flag) throws Exception{
    	SessionUser user = this.getUserFromSession(request);
    	String queryStr = initPageCom(request,pagesize);
    	Integer isremark_today = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
    	PagerVO pv  = this.wfReqCurrentoperatorMng.getOfficeTodayByUserId("wf_req_currentoperator", user.getId(), isremark_today, null, flag,null);
    	pv.addJsonParam("queryStr", queryStr);
    	return this.beanToJson(pv);
    }
	/**
	 * 待办事宜显示数字
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	private void getWaitEventNum(HttpServletRequest request, ModelMap model,SessionUser user) throws Exception{
		Integer userId = user.getId();
		//待办事宜
		Integer isremark = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
		Integer officeNum = this.wfReqCurrentoperatorMng.getNumByUserId("wf_req_currentoperator", userId, isremark, null, null,null,null);
		model.addAttribute("officeNum", officeNum);
		//今日新增
		Integer isremark_today = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
		Integer officeNum_today = this.wfReqCurrentoperatorMng.getNumByUserId("wf_req_currentoperator", userId, isremark_today, null, 3,null,null);
		model.addAttribute("officeNum_today", officeNum_today);
		//紧急
		Integer isremark_level2 = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
		Integer officeNum_level2 = this.wfReqCurrentoperatorMng.getNumByUserId("wf_req_currentoperator", userId, isremark_level2, null, 2,null,null);
		model.addAttribute("officeNum_level2", officeNum_level2);
		//退回
		Integer isremark_back = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_NOOPERATE);
		Integer officeNum_back = this.wfReqCurrentoperatorMng.getNumByUserId("wf_req_currentoperator", userId, isremark_back,WfWorkflowParamConst.ISYES, null, null,null);
		model.addAttribute("officeNum_back", officeNum_back);
		//进行中
		Integer isremark_now = this.wfWorkflowParamMng.getValueByCode(WfWorkflowParamConst.CURROPERISREMARK_ALREADYOPERATE);
		Integer officeNum_now = this.wfReqCurrentoperatorMng.getNumByUserId("wf_req_currentoperator", userId, isremark_now, null,WfWorkflowParamConst.ISNO, null,null);
		model.addAttribute("officeNum_now", officeNum_now);
	} 
	/**
	 * 快速通道
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void getExpressWay(HttpServletRequest request, ModelMap model,SessionUser user) throws Exception{
		Integer userId = user.getId();
		//360考核
		PagerVO pv_history = hrmReviewQuestionsMng.queryHistoryAssess(userId);
		model.addAttribute("test360_history", pv_history.getTotal());
		PagerVO pv_now = hrmReviewQuestionsMng.queryCurrentAssess(userId);
		model.addAttribute("test360_now", pv_now.getTotal());
		//我的资产
		IWSAssetBase assetBase =BeanFactory.getBean(IWSAssetBase.class);
		String dataString = assetBase.getPersonalAssets(userId, 0);
		List<Map<String, Object>> admPersonalAssetList=(List<Map<String, Object>>) JsonUtil.jsonToList(Util.null2String(dataString), Map.class);
		model.put("admPersonalAssetCount", null==admPersonalAssetList?0:admPersonalAssetList.size());
		//我的下属
		Integer  subordinateCount =  hrmResourceMng.findZhiJieXiaShuCount(userId);
		model.addAttribute("subordinateCount", subordinateCount);
		boolean subordinateRight =this.rightAuth(request, "hrm:subordinateView");
		model.put("subordinateRight", subordinateRight);
	}
	/**
	 * 最近审批
	 * @param request
	 * @param model
	 * @throws Exception
	 */
	private void getLastRequest(HttpServletRequest request, ModelMap model,SessionUser user) throws Exception{
		Integer userId = user.getId();
		//最近审批
		List<Map<String,Object>> lastRequestList = wfCustomListMng.getLastRequest(userId);
		if(lastRequestList!=null&&lastRequestList.size()>0){
			model.addAttribute("lastRequestList", lastRequestList);
		}
		//新办事宜类型
		EWfWorkflowType workflowType = new EWfWorkflowType();
		workflowType.setIsvalid(1);
		List<EWfWorkflowType> workflowTypes = workflowTypeMng.findByList(workflowType, "dsporder");
		model.put("workflowTypes", workflowTypes);
	}
	/**
     * 新办事宜
     * @param request
     * @param response
     * @param model
     * @return    
     * @author zhaolimin 
     * @throws Exception 
     * @date 2014-5-26 下午2:36:33 
     *
     */
    @RequestMapping(params = "method=newRequestGrid")
    @ResponseBody
    public String newRequestGrid(HttpServletRequest request,ModelMap model,
    		Integer pager_offset,Integer pagesize,String wfname,Integer typeid) throws Exception{
    	if(!Util.isBlank(wfname)){
    		wfname=URLDecoder.decode(wfname,"UTF-8");
    	}
    	String queryStr = initPageCom(request,pagesize);
        SessionUser user = (SessionUser) request.getSession(true).getAttribute(BaseAct.SESSION_USERNAME);
        Integer userId = user.getId();
        PagerVO pv = workflowTypeMng.newRequestGrid(userId,wfname,typeid);
        pv.addJsonParam("queryStr", queryStr);
        return this.beanToJson(pv);
    }
    /**
     * 获取广告
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "method=getIndexAdInfo")
    @ResponseBody
    public String getIndexAdInfo(HttpServletRequest request,ModelMap model) throws Exception{
    	String index_ad_num = this.dictSysParamMng.getValueByCode(SysParamConst.INDEX_AD_NUM);
    	initPageCom(request,Util.getIntegerValue(index_ad_num));
    	ESysAdvertisement sysAdvertisement=new ESysAdvertisement();
    	sysAdvertisement.setEnableflag(1);
        PagerVO pv = sysAdvertisementMng.findByPage(sysAdvertisement, "showorder");
        return this.beanToJson(pv);
    }
    

	
    /**
	 * 加载搜房各集团数字统计
	 * @param req
	 * @param model
	 * @throws Exception
	 */
	 void loadCompanyDigits(HttpServletRequest request,ModelMap model,SessionUser user) throws Exception{
		//1、是否关闭
		boolean colseDigits = new Boolean(true).equals(request.getSession(true).getAttribute("MainpageColseDigits"));
		model.put("colseDigits",colseDigits);
		//2、关闭时不查询数据
		if(colseDigits){
			return;
		}
		//3、是否有查看全部的权限
		boolean companyDigits_view = super.rightAuth(request, "companyDigits_view_all");
		List<String> rights = null;
		//4、无全部权限时按机构权限表分权
		if(!companyDigits_view){
			rights = getDigitalRight(user.getId());
		}
		boolean showDigits = companyDigits_view||rights.size()>0;
		model.put("showDigits", showDigits);
		//5、无权限时不需要查询数据
		if(!showDigits){
			return ;
		}
		//6、查询类别
		List<Map<String,Object>> tags = syncCompanyDigitsHMng.getTags();
		String[][] arr = new String[tags.size()][4];
		for(int i=0;i<tags.size();i++){
			Map<String,Object> map =tags.get(i); 
			arr[i][1] =  String.valueOf(map.get("company")).trim();
			arr[i][2] =  String.valueOf(map.get("category")).trim();
			arr[i][3] =  String.valueOf(map.get("name")).trim();
			arr[i][0] =  "digits@"+arr[i][1]+"@"+arr[i][2]+"@"+arr[i][3];
		}
		//7、循环查询所有类别数据（每一类别中今天最新一条）
		for(int i=0;i<arr.length;i++){
			String[] item = arr[i];
			String key = item[0];
			System.out.println(key+"}}");
			String company = item[1];//数字统计:所属公司
			String category = item[2];//数字统计:所属分类
			String name = item[3];//数字统计:所属子分类
			if(companyDigits_view){
				model.put(key, getSyncCompanyDigits(request, company,category,name));
				continue;
			}
			Iterator<String> it = rights.iterator();
			while(it.hasNext()){
				String _r = it.next();
				if((key+"@").startsWith(_r+"@")){
					model.put(key, getSyncCompanyDigits(request, company,category,name));
					break;
				}
			}
		}
		
	}

	/**
	 * 获取搜房各集团数字统计
	 * @param req 
	 * @param company 数字统计:所属公司
	 * @param category 数字统计:所属分类
	 * @param name 数字统计:所属子分类
	 * @return
	 */
	private ESyncCompanyDigitsH getSyncCompanyDigits(HttpServletRequest req,String company,String category,String name) throws Exception{
		ESyncCompanyDigitsH syncCompanyDigits = new ESyncCompanyDigitsH();
		//获取创建时间为[今天]中最新一条数据
//		syncCompanyDigits.setAdditionalWhereSQL("name='"+name+"' and category='"+category+"' and company='"+company+"' and substr(createdate,1,10)=to_char(sysdate,'yyyy-mm-dd')");
		syncCompanyDigits.setName(name);
		syncCompanyDigits.setCategory(category);
		syncCompanyDigits.setCompany(company);
		super.initPageCom(req, 1);
		PagerVO pv = syncCompanyDigitsHMng.findByPage(syncCompanyDigits, "createdate desc");
		if(pv.getTotal()==0){
			return null;
		}
		syncCompanyDigits = (ESyncCompanyDigitsH) pv.getDatas().get(0);
		syncCompanyDigits.setTodays(fmtMicrometer(syncCompanyDigits.getTodays()));
		syncCompanyDigits.setYesterdays(fmtMicrometer(syncCompanyDigits.getYesterdays()));
		syncCompanyDigits.setWeeks(fmtMicrometer(syncCompanyDigits.getWeeks()));
		syncCompanyDigits.setMonths(fmtMicrometer(syncCompanyDigits.getMonths()));
		syncCompanyDigits.setYears(fmtMicrometer(syncCompanyDigits.getYears()));
		syncCompanyDigits.setBudget(fmtMicrometer(syncCompanyDigits.getBudget()));
		
		return syncCompanyDigits;
	}
	/**
	 * 常见问题：分页查询-ajax获取数据
	 * 
	 * @param request
	 * @param model
	 * @param sysDocs
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getDocAjaxData")
	@ResponseBody
	public String getDocAjaxData(HttpServletRequest request, ModelMap model,
			ESysDocs sysDocs, String fileTypeCode,Integer pagesize,Integer pager_offset) throws Exception {
		SessionUser user = this.getUserFromSession(request);
		// 初始化分页设置
		String queryStr = initPageCom(request,pagesize);
		// vo属性解码，防止中文乱码
		sysDocs.decode();
		// 管理员权限
		boolean adminFlag = this.rightAuth(request, "SysDocAdmin");
		if(null != fileTypeCode ){
			Integer sysfiletypeid = this.sysFileTypeMng.getIdByTypeCode(fileTypeCode);
			sysDocs.setSysfiletypeid(sysfiletypeid);
		}
		PagerVO pv = new PagerVO();
		// 分页查询
		pv = this.sysDocsMng.findByPage(sysDocs, user.getId(),user.getSubcompanyid(), user.getDepartmentid(), adminFlag, null);
		// 设置额外的ajax参数
		pv.addJsonParam("queryStr", queryStr);
		return this.beanToJson(pv);
	}
	/**
	 * 日期转换
	 * @param text 值
	 * @return
	 */
	private static String fmtMicrometer(String text) {
		if (text == null){
			return null;
		}
		text = text.trim().replaceAll("[^.\\d\\-]", "");
		if (text.length() == 0){
			return null;
		}
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			return null;
		}
		return df.format(number);
	}
	/**
	 * 获取登录用户的首页统计数字权限
	 * @param uid 登录人id
	 * @return
	 * @throws Exception 
	 */
	private List<String> getDigitalRight(int uid) throws Exception{
		List<String> ret = new ArrayList<String>();
		//当前登录人有权限的分部ids
		String subids = ","+indexMng.getsubcoms(uid)+",";
		//查询统计数字与分部对应关系
		List<Map<String,Object>> list = syncCompanyRelateHMng.getRelateHNum();
		Iterator<Map<String,Object>> it = list.iterator();
		while(it.hasNext()){
			Map<String,Object> map = it.next();
			String path = String.valueOf(map.get("path"));
			String orgid = String.valueOf(map.get("orgid"));
			//有分部权限则有对应统计数字权限
			if(subids.toString().indexOf(","+orgid+",")>=0){
				ret.add(path);
			}
		}
		return ret;
	}
	
	/**
	 * 查询OA首页统计数字（城市排行数据）
	 * @param req
	 * @param res
	 * @param group
	 * @param category
	 * @param name
	 * @param freq
	 * @param callback
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(params="method=getDigtalCityForBusiness")
	public void getDigtalCityForBusiness(HttpServletRequest req,HttpServletResponse res,String group,String category,String name,String freq,String callback) throws Exception{
		String cityUrl = this.dictSysParamMng
				.getValueByCode(SysParamConst.INDEX_CITY);
		URL getUrl = new URL(cityUrl+"&group="+group+"&category="+category+"&name="+name+"&freq="+freq);
        HttpURLConnection connect = (HttpURLConnection) getUrl.openConnection();
        connect.setRequestMethod("GET");
        connect.setRequestProperty("Content-type", "text/xml");
        connect.setRequestProperty("Accept-Charset", "utf-8");
        connect.setRequestProperty("contentType", "utf-8");
        connect.setDoOutput(true);
        connect.connect();
        int httpcode = connect.getResponseCode();
        if (httpcode == 200) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		int i = -1;
    		InputStream isInputStream = connect.getInputStream();
    		while ((i = isInputStream.read()) != -1) {
    			baos.write(i);
    		}
    		res.getOutputStream().write((callback+"("+baos.toString("utf-8")+");").getBytes("gbk"));
        }
        // 断开连接
        connect.disconnect();
	}
	
	@ResponseBody
	@RequestMapping(params="method=colseDigits")
	public boolean colseDigits(HttpServletRequest req,HttpServletResponse res)throws Exception{
		req.getSession(true).setAttribute("MainpageColseDigits", true);
		return true;
	}
	/**
	 * 初始页本年数据组成分月显示
	 * @author chenxuguang
	 * @since 2015-4-2下午1:45:45
	 * @param req
	 * @param res
	 * @param company
	 * @param category
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(params="method=getDigtalSepMonthData")
	public List<String> getDigtalSepMonthData(HttpServletRequest req,HttpServletResponse res,String company,String category,String name)throws Exception{
		List<String> list = new ArrayList<String>();
		ESyncCompanyDigitsH syncCompanyDigits = new ESyncCompanyDigitsH();
		if (StringUtils.isBlank(category)||StringUtils.isBlank(name)) {
			return list;
		}
		category = URLDecoder.decode(category.toString(),"utf-8");
		name = URLDecoder.decode(name.toString(),"utf-8");
		
		Calendar cal = Calendar.getInstance();
		Integer nowMonth = cal.get(Calendar.MONTH)+1;
		for (int i = 1; i <= nowMonth; i++) {
			syncCompanyDigits.setAdditionalWhereSQL("name='"+name+"' and category='"+category+"' and company='"+company+"' and substr(createdate,0,7) = '"+cal.get(Calendar.YEAR)+"-"+(i>=10?i:"0"+i)+"' ");
			super.initPageCom(req, 1);
			PagerVO pv = syncCompanyDigitsHMng.findByPage(syncCompanyDigits, "createdate desc");
			List<ESyncCompanyDigitsH> syncCompanyDigitsHs = (List<ESyncCompanyDigitsH>) pv.getDatas();
			if (syncCompanyDigitsHs.size()>0) {
				ESyncCompanyDigitsH companyDigitsH = syncCompanyDigitsHs.get(0);
				if(companyDigitsH.getMonths()==null){
					list.add("");
				}else {
					list.add(companyDigitsH.getMonths().trim().replaceAll("[^.\\d\\-]", ""));
				}
			}else {
				list.add("");
			}
		}
		return list;
	}
	/**
	 * 工作台请求
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getWorkbench")
	public String getWorkbench(HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		SessionUser user = this.getUserFromSession(request);
		String serverName = request.getServerName();
		String firstDomain = serverName.replaceAll(".*\\.(\\w+\\.\\w+)$", "$1");//一级域名\
		int userId = user.getId();
		String callback = request.getParameter("callback");
		String clrsso = request.getParameter("clrsso");
		//clrsso = "1";
		if("1".equals(clrsso)){
			MemClient.remove(MemKeyUtil.ssoDataAndCodePrefix+userId);
		}
		String data = (String)MemClient.get(MemKeyUtil.ssoDataAndCodePrefix+userId);
		if(data!=null){
			String[] arr = data.split("@@");
			data = arr[0];
			String code = arr[1];
			String userName = user.getLoginid();
			Cookie useridcookie = new Cookie("new_zcb",userId+"|"+userName+"|"+code.toString());
			useridcookie.setDomain(firstDomain);
			useridcookie.setMaxAge(-1);
			useridcookie.setPath("/");
			response.addCookie(useridcookie);
			Cookie useridcookie2 = new Cookie("ssoSessionId",code);//用于bbs的sso登录
			useridcookie2.setMaxAge(-1);//-1表示浏览器关闭时清除
			useridcookie2.setPath("/");
			response.addCookie(useridcookie2);
			out.write(callback+data);
			return null;
		}
			String userName = user.getLoginid();
			String s = UUID.randomUUID().toString();
			String code = s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
			String time = String.valueOf(System.currentTimeMillis());
			Cookie useridcookie = new Cookie("new_zcb",userId+"|"+userName+"|"+code.toString());
			useridcookie.setDomain(firstDomain);
			useridcookie.setMaxAge(6*60*60);
			useridcookie.setPath("/");
			
			ESyscookies syscookies = new ESyscookies();
			syscookies.setCode(code);
			syscookies.setTime(time);
			syscookies.setUsername(userName);
			syscookies.setUserid(userId);
			this.sysCooKiesMng.insertVO(syscookies);
			response.addCookie(useridcookie);
			Cookie useridcookie2 = new Cookie("ssoSessionId",code);//用于bbs的sso登录
			useridcookie2.setMaxAge(-1);//-1表示浏览器关闭时清除
			useridcookie2.setPath("/");
			response.addCookie(useridcookie2);
			URL getUrl = new URL(this.dictSysParamMng
					.getValueByCode(SysParamConst.INDEX_WORKBENCH));
		    HttpURLConnection connect = (HttpURLConnection) getUrl.openConnection();
		    connect.setRequestMethod("GET");
		    connect.setRequestProperty("Content-type", "text/xml");
		    connect.setRequestProperty("Accept-Charset", "utf-8");
		    connect.setRequestProperty("contentType", "utf-8");
		    connect.setDoOutput(true);
		    connect.setRequestProperty("Cookie", "new_zcb="+userId+"|"+userName+"|"+code.toString());
		    connect.connect();
		    int httpcode = connect.getResponseCode();
		    if (httpcode == 200) {
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = -1;
				InputStream isInputStream = connect.getInputStream();
				while ((i = isInputStream.read()) != -1) {
					baos.write(i);
				}
				data = baos.toString("gbk");
				if(data.indexOf("error")==-1&&data.indexOf("errmsg")==-1)
					MemClient.set(MemKeyUtil.ssoDataAndCodePrefix+userId, data+"@@"+code, 1000*60*30);
				out.write(callback+data);
		    }
		    // 断开连接
		    connect.disconnect();
		    return null;
	}
	
	/**
	 *  修改密码
	 * @discription 
	 * @author shangwenyu       
	 * @created 2015-4-17 上午11:20:59     
	 * @param request
	 * @param model
	 * @param passwordold
	 * @param passwordnew
	 * @param confirmpassword
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params="method=editPasswd")
	public String editPasswd(HttpServletRequest request,ModelMap model,
			String passwordold,String passwordnew,String confirmpassword) throws Exception{
		ResultInfo ri = new ResultInfo();
		SessionUser user = this.getUserFromSession(request);
		ri = hrmResourceMng.updatePasswd(user,passwordold, passwordnew, confirmpassword);
		return this.beanToJson(ri);
	}
	/**
	 * 修改座机
	 * @discription 
	 * @author shangwenyu       
	 * @created 2015-4-18 上午11:20:23     
	 * @param request
	 * @param model
	 * @param id
	 * @param phone
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params="method=editTelePhone")
	public ResultInfo editTelePhone(HttpServletRequest request,ModelMap model,
			Integer id,String phone,HttpServletResponse response) throws Exception{
		//座机修改
		SessionUser user = super.getUserFromSession(request);
		return hrmResourceMng.updatePhone(id, phone, user);
	}
	/**
	 * 发送验证码
	 * @discription 
	 * @author shangwenyu       
	 * @created 2015-4-18 上午10:42:36     
	 * @param request
	 * @param model
	 * @param userid
	 * @param mobile手机号
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params="method=sendchkcode")
	public ResultInfo sendchkcode(HttpServletRequest request,ModelMap model,
			Integer userid, String mobile) throws Exception{
		HttpSession s = request.getSession();

		return hrmResourceMng.sendchkcode(mobile, s);
	}
	/**
	 * 修改手机号
	 * @discription 
	 * @author shangwenyu       
	 * @created 2015-4-18 上午11:08:02     
	 * @param request
	 * @param response
	 * @param userid
	 * @param mobile
	 * @param checkcode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(params="method=editMobile")
	public ResultInfo editMobile(HttpServletRequest request,HttpServletResponse response,
			Integer userid,String mobile,String checkcode) throws Exception{
		SessionUser user = super.getUserFromSession(request);
		//真实验证码
		String chkcode = (String)super.getAttrFromSession(request, "chkcode");
		if(checkcode.equalsIgnoreCase(chkcode)) {
			//手机修改
			return hrmResourceMng.updateMobile(userid,mobile,user);
		} else {
			return  new ResultInfo(-1,"验证码不正确");
		}
	}
	
	/**
	 * 验证能否查看高管部门户
	 * @discription 
	 * @author shangwenyu       
	 * @created 2015-4-28 上午10:58:02     
	 * @param user
	 * @return
	 */
	private boolean validRight(SessionUser user) {
		boolean right = false;
		
		if (null != user && ( user.getSeclevel() > 25 ||user.getId() == 5743||user.getId() == 14963||user.getId() == 23481 || user.getId() == 184|| user.getId() == 19679)) {
			right = true;
		}
		
		return right;
	}
}
