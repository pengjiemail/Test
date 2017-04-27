package te;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class JsonUtil {

	/**
	 * 将bean转换为json对象 要转换的bean如果为对象，对象中的属性必须有public的get方法
	 * 
	 * @param obj
	 *            要转换的对象
	 * @return
	 * @throws Exception
	 */
	public static String beanToJson(Object obj) throws Exception {
		return beanToJson(obj, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将bean转换为json对象 要转换的bean如果为对象，对象中的属性必须有public的get方法
	 * 
	 * @param obj
	 *            要转换的对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String beanToJson(Object obj, String dateFormatStr)
			throws Exception {
		if (null == obj) {
			return "";
		}
		JsonGenerator gen = null;
		StringWriter writer = null;
		String json = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			// 个性化设置
			mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES,
					false);
			mapper.getSerializationConfig().setSerializationInclusion(
					Inclusion.NON_NULL);
			// 格式化日期
			mapper.setDateFormat(new SimpleDateFormat(dateFormatStr));
			writer = new StringWriter();
			gen = new JsonFactory().createJsonGenerator(writer);
			mapper.writeValue(gen, obj);
			json = writer.toString();
		} catch (Exception e) {
		} finally {
			if (null != gen) {
				gen.close();
			}
			if (null != writer) {
				writer.close();
			}
		}
		json = json.replaceAll("'", "&#39;").replaceAll(":null", ":\"\"");
		return json;

	}

}
