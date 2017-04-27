package work.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import work.entity.sync.ESyncCompanyCityH;
import work.service.sync.SyncCompanyCityHMng;
import core.interfaces.CompanyDigitsHUtil;
import core.servlet.InterfacesBase;
import core.util.BeanFactory;
import core.util.JsonUtil;
import core.util.ResultInfo;
import core.util.Util;

/**
 * 搜房统计数据（城市）
 * @author Qinzisong
 *
 */
@SuppressWarnings("serial")
public class GetDigtalCityForBusiness extends InterfacesBase {

	@Override
	public boolean doInterfaces(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ResultInfo info = super.checkUserValid(req, res);
		String callback = req.getParameter("callback");//jsonp
		String encode = req.getParameter("encode");//编码
		if (info.getCode() == 1) {
			SyncCompanyCityHMng mng = BeanFactory.getBean(SyncCompanyCityHMng.class);
			String group = req.getParameter("group");
			String category = req.getParameter("category");
			String name = req.getParameter("name");
			String freq = req.getParameter("freq");//0:月；1：年
			if (Util.isBlank(group) || Util.isBlank(category) || Util.isBlank(name)) {
				info.setCode(-1);
				info.setMessage("参数不正确");
			}else {
				group = URLDecoder.decode(group,"utf-8");
				category = URLDecoder.decode(category,"utf-8");
				name = URLDecoder.decode(name,"utf-8");
				ESyncCompanyCityH cityH = new ESyncCompanyCityH();
				cityH.setGroupname(group);
				cityH.setCategory(category);
				cityH.setName(name);
				try {
					List<Map<String, Object>> list = mng.getNewestCityData(cityH);
					List<Map<String, String>> cityList = new ArrayList<Map<String,String>>();
					for (Map<String, Object> bean : list) {
						cityH = mng.getVO(ESyncCompanyCityH.class, ((BigDecimal)bean.get("ID")).intValue());
						Map<String, String> map = new HashMap<String, String>();
						cityList.add(map);
						map.put("City", cityH.getCity());
						if ("1".equals(freq)) {
							map.put("Title", "本年");
							map.put("Value", cityH.getYears().replaceAll("[^.\\d\\-]", ""));
						}else {
							map.put("Title", "本月");
							map.put("Value", cityH.getMonths().replaceAll("[^.\\d\\-]", ""));
							map.put("Budget", (cityH.getBudget()+"").replaceAll("[^.\\d\\-]", ""));
						}
					}
					Map<String, Object> rMap = new HashMap<String, Object>();
					rMap.put("Business", CompanyDigitsHUtil.getCompanyName(group) + category);
					rMap.put("Title", name);
					rMap.put("Rows", cityList);
					info.setData(JsonUtil.beanToJson(rMap));
				} catch (Exception e) {
					info.setCode(-1);
					info.setMessage(e.getMessage());
				}
			}		
		}
		if (1 == info.getCode()) {
			if(callback!=null){
				info.setData(callback+"("+info.getData()+");");
			}
			if(encode==null){
				encode = "utf-8";
			}
			res.getOutputStream().write(((String)info.getData()).getBytes(encode));
		}else {
			res.getOutputStream().write(info.getMessage().getBytes("utf-8"));
		}
		return true;
	}

}
