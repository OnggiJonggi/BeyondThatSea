package sea.that.beyond.memeber.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.dao.MemberDao;
import sea.that.beyond.memeber.model.vo.Signup;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private MemberDao dao;

	/**
	 * 회원가입
	 * @param Signup
	 * @return 있겠냐?
	 */
	@Override
	public void signup(Signup signup) throws Exception{
		/*
		 * 비밀번호 암호화... 필요한가?
		 * 그런 하남자슈퍼겁쟁이에겐남스럽게
		 * 혹시 유출되면 어떠케하는 걱정은 하지 않는다.
		 */
		if(!(dao.signup(sqlSession, signup)>0))
			throw new Exception("저장 실패!"); 
	}
	
	
	/**
	 * 로그인
	 * @param Signup
	 * @return 있겠냐고
	 */
	@Override
	public void login(HttpSession session, Signup signup) throws Exception {
		if(dao.login(sqlSession, signup)>0)
			session.setAttribute("loginUser", signup.getId());
		else throw new Exception("해당하는 회원이 없는데요");
	}

}
