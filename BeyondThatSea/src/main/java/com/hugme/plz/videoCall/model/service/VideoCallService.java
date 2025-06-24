package com.hugme.plz.videoCall.model.service;

import java.util.List;
import java.util.Map;

import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;

public interface VideoCallService {
	
	//이미 자신이 몇 개의 방을 생성했는지 확인
	int countMyVcRoom(HttpSession session);
	
	//새로운 방 생성
	int createRoom(HttpSession session, VideoCall vc) throws Exception;

	//자신의 방 리스트 조회
	List<VideoCall> myRoomList(HttpSession session);
	
	//openvidu 토큰 생성
	Map<String, Object> createToken(HttpSession session, String sessionId) throws Exception;



}
