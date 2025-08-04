package com.hugme.plz.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;


//컨트롤러에 공통적으로 적용해야하는 기능을 모아 처리하는 클래스에 부여하는 어노테이쑌
//공통 예외처리 또는 공통 모델 데이터 등록같이 공통적으로 필요한 기능 부여
//컨트롤러 요청이 들어오면 동작하는 도구
@ControllerAdvice
public class ControllerAdvise {
	//아래와 같이 세션에 담긴 데이터를 모델에 담아 보내면
	//view에서 ${키값}으로 접근가능
	@ModelAttribute("loginUser") //이 메소드가 반환하는 값을 "loginUser"라는 이름으로 model에 추가함
	public Void addLoginUser(HttpSession session) {
		//session에 loginUser가 있는지 판별 수 리턴
		if(session.getAttribute("loginUser") != null) {
			return null;
		} else {
			return null;
		}
	}
}
