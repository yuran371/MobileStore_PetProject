package utlis;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JspHelper {

	private final String URL = "WEB-INF/JSP/%s.jsp";

	public String getUrl(String jspPageName) {
		return String.format(URL, jspPageName);
	}
}
