package com.hugme.plz.videoCall.model.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.hugme.plz.common.ControllerAdvise;
import com.hugme.plz.common.Regexp;
import com.hugme.plz.common.UuidUtil;
import com.hugme.plz.member.model.vo.Member;
import com.hugme.plz.videoCall.model.dao.VideoCallDao;
import com.hugme.plz.videoCall.model.vo.VcMember;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;


@Service
public class VideoCallServiceImpl implements VideoCallService{

    private final ControllerAdvise controllerAdvise;
	@Autowired
	private SqlSessionTemplate sqlSession;
    @Autowired
    private DailyService dailyService;
    
	@Autowired
	private VideoCallDao dao;


    VideoCallServiceImpl(ControllerAdvise controllerAdvise) {
        this.controllerAdvise = controllerAdvise;
    }
	
	
	//페이지 접근시 자신의 방 리스트를 보여준다.
	@Override
	public void myRoomList(HttpSession session) throws Exception{
		Member m = (Member)session.getAttribute("loginUser");
		List<VideoCall> list = dao.myRoomList(sqlSession, m);
		
		for(VideoCall vc : list) {
			//방 리스트에 이름/시드 넣기
			vc.setUserName(m.getUserName());
			vc.setNameSeed(m.getNameSeed());
			
			//UUID byte[] -> String(하이픈 포함)
			vc.setVcIdStr(UuidUtil.byteArrToStr(vc.getVcId()));
		}
		
		System.out.println("자신이 생성한 방 : "+list);
		
		
		//세션에 퐁당
		session.setAttribute("myVcRoom", list);
	}
	
	//초대받은 방 조회
	@Override
	public void myInvitedRoomList(HttpSession session, Model model) throws Exception{
		Member m = (Member)session.getAttribute("loginUser");
		List<VideoCall> list = dao.myInvitedRoomList(sqlSession, m);
		
		List<VideoCall> invitedRoom =new ArrayList<>();
		List<VideoCall> allowedRoom =new ArrayList<>();
		for(VideoCall vc : list ) {
			if(vc.getRoleTye()==null) {
				//관리 권한이 없다면 초대 수락하지 않은 방
				invitedRoom.add(vc);
			}else {
				//관리 권한이 있으면 초대 수락한 방
				allowedRoom.add(vc);
			}
			//UUID byte[] -> String(하이픈 포함)
			vc.setVcIdStr(UuidUtil.byteArrToStr(vc.getVcId()));
		}
		System.out.println("자신이 초대받은 : "+list);
		
		//아직 초대 수락하지 않은 방
		model.addAttribute("invitedRoom", invitedRoom);
		
		//초대 수락한 방
		model.addAttribute("allowedRoom", allowedRoom);
		
		
	}
	
	//새로운 방 생성
	@Override
	@Transactional
	public int createRoom(HttpSession session, Model model, VideoCall vc) throws Exception{
		
		Member m = (Member)session.getAttribute("loginUser");
		
		//유효성 확인
		String maxParticipants = String.valueOf(vc.getMaxParticipants());
		String vcName = vc.getVcName();
		if(!maxParticipants.matches(Regexp.MAXPARTICIPANTS)
				|| !vcName.matches(Regexp.VCNAME)
				|| dao.countMyVcRoom(sqlSession,m)>5) return 0;
		
		vc.setUserNo(m.getUserNo());
		
		//uuid생성하기
		Map<String, Object> createdUuid = UuidUtil.createUUID();
		
		//byte[]형식 uuid를 DB에 저장
		byte[] vcId = (byte[])createdUuid.get("uuidRaw");
		vc.setVcId(vcId);
		
		//문자열(하이픈 포함) uuid를 daily.co로 보내기
		String uuidStr = (String)createdUuid.get("uuid");
		
		// 관리자용 토큰 생성
		String ownerToken = dailyService.createMeetingToken(uuidStr, m, "owner").block();
		if(ownerToken==null) return 0;
		
		//daily.co에 방 생성(방 url)
        String createdRoom = dailyService.createRoom(uuidStr, vc);
        if(createdRoom==null) return 0;
        
        //방 url과 토큰을 합치기
        String accessUrl = createdRoom + "?t=" + ownerToken;
        System.out.println("새로운 방이 생성되었어요! : " + accessUrl);
        
		//데이터 모음집 넣기
		vc.setUserName(m.getUserName());
		vc.setNameSeed(m.getNameSeed());
		
		//db에 방 생성하고 참여자 목록에 추가
		int result = dao.createRoom(sqlSession,vc);
		if(result>0) {
			VcMember vcm = VcMember.builder()
					.vcId(vcId)
					.userNo(m.getUserNo())
					.status("Y")
					.roleType("O")
					.build();
			result = dao.insertVcMember(sqlSession,vcm);
			
			//모달에 해당 방 정보 저장
			model.addAttribute("createdVcRoomUrl", accessUrl);
			model.addAttribute("createdVcRoom", vc);
			
			//방 목록 재조회
			myRoomList(session);
		}
		
		return result;
	}
	
	//기존 자신의 방 활성화
	@Transactional
	@Override
	public int recallRoom(HttpSession session, VideoCall vc) throws Exception{
		Member m = (Member)session.getAttribute("loginUser");
		
		//vcIdstr을 vcId로 바꿔요.
		vc.setVcId(UuidUtil.strToByteArr(vc.getVcIdStr()));
		
		//먼저 유효성 확인 - 해당하는 방이 존재하는지 함 봅시다.
		List<VideoCall> list = (List<VideoCall>)session.getAttribute("myVcRoom");
		if(list==null || list.isEmpty()) return 0;
		
		//vcId로 방에 들어갈 거에요.
		boolean flag = false;
		for(VideoCall vcFor : list) {
			if(Arrays.equals(vcFor.getVcId(), vc.getVcId())) {
				vc = vcFor;
				flag = true;
				break;
			}
		}
		if(!flag) return 0;

		//
		
		//db에 저장하지 않는 프론트용 데이터
		vc.setUserName(m.getUserName());
		vc.setNameSeed(m.getNameSeed());
		
		//db에 방 세션 갱신하고 참여자 목록 갱신
		int result = dao.updateRoom(sqlSession,vc);
		if(result>0) {
			VcMember vcm = VcMember.builder()
					.userNo(m.getUserNo())
					.status("Y")
					.roleType("M")
					.build();
			result = dao.updateParticipate(sqlSession,vcm);
			
			//세션에 해당 방 정보 저장
			session.setAttribute("videoCallRoom", vc);
		}
		
		return result;
	}
	
	//초대받은 방 들어가기
	@Override
	public int goInvitedRoom(HttpSession session, String roomHref) throws Exception {
		int result = 1;
		Member m = (Member)session.getAttribute("loginUser");
		
		//roomHref를 vcId로 만들기
		byte[] vcId = roomHref.getBytes(StandardCharsets.UTF_8);
		
		//이놈 이거 접근 권한 진짜 있어?
		VcMember vcm = VcMember.builder()
				.vcId(vcId)
				.userNo(m.getUserNo())
				.build();
        String roleType = dao.haveLicense(sqlSession,vcm);
        
        //권한 없으면 가세요라
        if(roleType==null) return 0;
        
        //초대 수락 대기중 / 비접송 상태라면 상태 "Y"로 변경
        else if(roleType.equals("U") || roleType.equals("N")){
        	result *= dao.goInvitedRoom(sqlSession, vcm);
        }
		
        //해당 방 조회해서 세션에 넣기
        VideoCall vc = VideoCall.builder().vcId(vcId).build();
        vc = dao.selectRoom(sqlSession, vc);
		session.setAttribute("videoCallRoom", vc);
		
		return result;
	}
	
	//초대
	@Override
	public int inviteUser(HttpSession session, String userId, String vcIdStr) throws Exception {
		String hostUserNo = ((Member)session.getAttribute("loginUser")).getUserNo();
		byte[] vcId;
		int result = 1;
		
		//영상 통화방 안에서 초대 - session의 videoCallRoom에서 vcId추출
		//영상 통화방 밖에서 초대 - requestparm vcid이용
		
		if(vcIdStr==null) { //영상 통화방 안에서 초대
			vcId = ((VideoCall)session.getAttribute("videoCallRoom")).getVcId();
			
		}else {//영상 통화방 밖에서 초대
			vcId = vcIdStr.getBytes();
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("vcId", vcId);
		map.put("hostUserNo", hostUserNo);
		
		//초대한 유저가 해당 방의 접근 권한이 있는지 확인
		VcMember vcm = VcMember.builder()
				.userNo(hostUserNo)
				.vcId(vcId)
				.build();
		String roleType = dao.haveLicense(sqlSession, vcm);
		if(roleType == null || roleType.equals("S")) return 0; //없으면 가라
		
		//초대받은 유저 상태 'U'(초대 수락하지 않음)으로 VC_MEMBER에 넣기
		result *= dao.insertSubscriber(sqlSession, map);
		
		return result;
	}
	
}
