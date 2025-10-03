package sea.that.beyond.common.advisor;

import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class RootcontextAdvisor {
	//root-context 넣자
	@ModelAttribute("rootContextPath")
	public String getRoot(HttpSession session, HttpServletRequest request) {
		try {
	        String contextPath = request.getContextPath();
	        session.setAttribute("rootContextPath", contextPath);
	        return contextPath;
		} catch (Exception e) {
			return "/beyond";
		}
	}
}
