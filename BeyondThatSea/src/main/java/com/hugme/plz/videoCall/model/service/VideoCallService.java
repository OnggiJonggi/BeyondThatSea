package com.hugme.plz.videoCall.model.service;

import java.util.Map;

import org.springframework.ui.Model;

import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;

public interface VideoCallService {
	
	//자신의 방 리스트 조회
	void myRoomList(HttpSession session) throws Exception;
	
	//초대받고 수락 대기중인 방 조회
	void myInvitedRoomList(HttpSession session, Model model) throws Exception;
	
	//새로운 방 생성
	int createRoom(HttpSession session, Model model, VideoCall vc) throws Exception;
	
	//기존 자신의 방 활성화
	int recallRoom(HttpSession session, VideoCall vc) throws Exception;
	
	//초대하기
	int inviteUser(HttpSession session, String userId, String vcIdStr) throws Exception;

	//다른 방 참여하기
	int goInvitedRoom(HttpSession session, String roomHref) throws Exception;





}
