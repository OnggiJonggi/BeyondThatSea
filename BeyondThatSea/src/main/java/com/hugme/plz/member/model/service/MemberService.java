package com.hugme.plz.member.model.service;

import com.hugme.plz.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

public interface MemberService {

	//회원가입
	int enroll(Member m);
	
	//비동기 - 아이디 체크
	String checkUserId(String userId);

	//로그인
	int login(HttpSession session, Member m);

	
}
