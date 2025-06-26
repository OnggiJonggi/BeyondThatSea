package com.hugme.plz.member.model.service;

import com.hugme.plz.common.Regexp;
import com.hugme.plz.config.OpenViduConfig;
import com.hugme.plz.member.model.dao.MemberDao;
import com.hugme.plz.member.model.vo.Member;
import io.openvidu.java.client.OpenVidu;
import jakarta.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{

    private final OpenViduConfig openViduConfig;

    private final OpenVidu openViduBean;
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private MemberDao dao;
	@Autowired
	private BCryptPasswordEncoder bcrypt;

    MemberServiceImpl(OpenVidu openViduBean, OpenViduConfig openViduConfig) {
        this.openViduBean = openViduBean;
        this.openViduConfig = openViduConfig;
    }
	
	//회원가입
	@Override
	public int enroll(Member m) {
		String userId = m.getUserId().trim();
		String userPwd = m.getUserPwd().trim();
		String userName = m.getUserName().trim();
		
		//유효성 확인
		if(!userId.matches(Regexp.USERID)
				|| !userPwd.matches(Regexp.USERPWD)
				|| !userName.matches(Regexp.USERNAME)
				|| dao.checkUserId(sqlSession, userId)>0) {
			return 0;
		}
		
		//비번 암호화
		userPwd = bcrypt.encode(userPwd);
		
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setUserName(userName);
		m.setNameSeed(Regexp.createUUID());
		
		int result = dao.enroll(sqlSession, m);
		
		if(result>0) {
			return 1;
		}else {
			return 0;
		}
	}
	
	//비동기 - 아이디 체크
	@Override
	public String checkUserId(String userId) {
		if(!userId.matches(Regexp.USERID)) {
			return "noPass";
		}else if(dao.checkUserId(sqlSession, userId)==0){
			return "pass";
		}else {
			return "nopass";
		}
	}
	
	//로그인
	@Override
	public int login(HttpSession session, Member m) {
		String userId = m.getUserId().trim();
		String userPwd = m.getUserPwd().trim();
		
		if(!userId.matches(Regexp.USERID)
				|| !userPwd.matches(Regexp.USERPWD)) return 0;
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		
		Member loginUser = dao.login(sqlSession, m);
		
		if(!bcrypt.matches(userPwd, loginUser.getUserPwd())) return 0;
		
		session.setAttribute("loginUser", loginUser);
		return 1;
	}
}
