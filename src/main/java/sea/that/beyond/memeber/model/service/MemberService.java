package sea.that.beyond.memeber.model.service;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.vo.Signup;

public interface MemberService{

	public void signup(Signup signup) throws Exception;

	public void login(HttpSession session, Signup signup) throws Exception;

}
