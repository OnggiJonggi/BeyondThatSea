package sea.that.beyond.memeber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.service.MemberService;
import sea.that.beyond.memeber.model.vo.Signup;

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
	public String signup(HttpSession session, Signup signup) {
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
	public String login(HttpSession session, Signup signup) {
		try {
			service.login(session, signup);
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
