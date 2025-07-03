package com.hugme.plz.videoCall.model.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hugme.plz.videoCall.model.service.VideoCallService;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/videoCall")
public class VideoCallController {
	@Autowired
	private VideoCallService service;
	
	//영상통화 메인
	@GetMapping("/vcMain")
	public String goVideoCallMain(HttpSession session, Model model) {
		try {
			//자신의 방 조회
			service.myRoomList(session);
			
			//초대받은 방 조회
			service.myInvitedRoomList(session,model);
			
			return "videoCall/vcMain";
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	//새로운 방 생성
	@ResponseBody
	@PostMapping("/createRoom")
	public String createRoom(HttpSession session, Model model, VideoCall vc) {
		try {
			int result = service.createRoom(session,model,vc);
			if(result>0) {
				//자신의 방 리스트 다시 조회
				service.myRoomList(session);
				
				return "success";
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "fail";
		}
	}
	
	//기존 자신의 방 활성화
	@PostMapping("/recallRoom")
	public String recallRoom(HttpSession session, VideoCall vc) {
		try {
			int result = service.recallRoom(session, vc);
			if(result>0) {
				return "redirect:/videoCall/room";
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	//참여한 방 들어가기
	@PostMapping("/goInvitedRoom")
	public String goInvitedRoom(HttpSession session, String roomHref) {
		try {
			int result = service.goInvitedRoom(session, roomHref);
			if(result>0) {
				return "redirect:/videoCall/room";
			}else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMsg", "500 err");
			return "common/errorPage";
		}
	}
	
	//방 초대하기
	@ResponseBody
	@PostMapping("/inviteUser")
	public String inviteUser(HttpSession session, String userId, String vcIdStr) {
		try {
			int result = service.inviteUser(session, userId, vcIdStr);
			if(result>0) {
				return "success";
			}else {
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
	
	//비동기 - 방 참여하기
	@ResponseBody
	@PostMapping("/participate/{sessionId}")
	public Map<String, Object> createToken(@PathVariable String sessionId
			,HttpSession session) {
		Map<String, Object> result = null;
	    try {
	        return result;
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = new HashMap<>();
	        result.put("error", "토큰 생성 실패");
	        return result;
	    }
	}
}
