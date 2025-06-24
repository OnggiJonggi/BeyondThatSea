package com.hugme.plz.videoCall.model.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hugme.plz.videoCall.model.service.VideoCallService;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;


@Controller
public class VideoCallController {
	@Autowired
	private VideoCallService service;
	
	//영상통화 메인
	@GetMapping("videoCall/vcMain")
	public String goVideoCallMain(HttpSession session, Model model) {
		try {
			int result = service.countMyVcRoom(session);
			model.addAttribute("countMyVcRoom",result);
			return "videoCall/vcMain";
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	//새로운 방 생성
	@PostMapping("videoCall/createRoom")
	public String createRoom(HttpSession session, Model model, VideoCall vc) {
		try {
			int result = service.createRoom(session,vc);
			if(result>0) {
				return "videoCall/room";
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		} 
	}
	
	//비동기 - 자신의 방 목록 보여주기
	@ResponseBody
	@PostMapping("videoCall/myRoomList")
	public String myRoomList(HttpSession session) {
		try {
			List<VideoCall> list = new ArrayList<>();
			list = service.myRoomList(session);
			
			if(list==null) {
				throw new Exception();
			}else {
				return "videoCall/room";
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	
	//기존 방 불러오기
	@GetMapping("videoCall/recallRoom")
	public String recallRoom(HttpSession session, VideoCall vc) {
		try {
			int result = service.recallRoom(session, vc);
			if(result>0) {
				return "videoCall/room";
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	//방 참여하기
	@ResponseBody
	@PostMapping("videoCall/participate/${sessionId}")
	public Map<String, Object> createToken(@PathVariable String sessionId
			,HttpSession session) {
		Map<String, Object> result = null;
	    try {
	    	result = service.createToken(session, sessionId);
	        return result;
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = new HashMap<>();
	        result.put("error", "토큰 생성 실패");
	        return result;
	    }
	}
}
