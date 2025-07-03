package com.hugme.plz.member.model.service;

import com.hugme.plz.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

public interface MemberService {

	//회원가입
	int enroll(Member m) throws Exception;
	
	//비동기 - 아이디 체크
	String checkUserId(String userId) throws Exception;

	//로그인
	int login(HttpSession session, Member m) throws Exception;

	
}
