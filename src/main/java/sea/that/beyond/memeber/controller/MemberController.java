package sea.that.beyond.memeber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.service.MemberService;
import sea.that.beyond.memeber.model.vo.UserIn;
import sea.that.beyond.memeber.model.vo.UserOut;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberService service; 
		
	@ResponseBody
	@GetMapping("/signup")
	public String goSignup() {
		return "member/signup";
	}

	@ResponseBody
	@PostMapping("/signup")
	public String signup(HttpSession session, UserIn signup) {
		try {
			service.signup(signup);
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "계정 생성 실패!");
		}
		return "redirect:/";
	}
	
	@ResponseBody
	@GetMapping("/login")
	public String goLogin() {
		return "member/login";
	}
	
	@ResponseBody
	@PostMapping("/login")
	public String login(HttpSession session, UserOut userOut) {
		try {
			service.login(session, userOut);
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "로그인 실패!");
		}
		return "redirect:/";
	}
	
	@ResponseBody
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
