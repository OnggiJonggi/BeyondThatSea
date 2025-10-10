package sea.that.beyond.memeber.model.service;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.vo.UserIn;
import sea.that.beyond.memeber.model.vo.UserOut;

public interface MemberService{

	public void signup(UserIn signup) throws Exception;

	public void login(HttpSession session, UserOut userOut) throws Exception;

}
