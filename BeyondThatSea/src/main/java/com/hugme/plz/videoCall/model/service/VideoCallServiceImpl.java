package com.hugme.plz.videoCall.model.service;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hugme.plz.common.Regexp;
import com.hugme.plz.config.VideoCallConfig;
import com.hugme.plz.member.model.vo.Member;
import com.hugme.plz.videoCall.model.dao.VideoCallDao;
import com.hugme.plz.videoCall.model.vo.VcMember;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import jakarta.servlet.http.HttpSession;



@Service
public class VideoCallServiceImpl implements VideoCallService{
	@Autowired
	private SqlSessionTemplate sqlSession;
    @Autowired
    private VideoCallConfig vcConfig;
    @Autowired
    private DailyService dailyService;
    
	@Autowired
	private VideoCallDao dao;
	
	
	//페이지 접근시 자신의 방 리스트를 보여준다.
	@Override
	public void myRoomList(HttpSession session) throws Exception{
		Member m = (Member)session.getAttribute("loginUser");
		List<VideoCall> list = dao.myRoomList(sqlSession, m);
		
		//방 리스트에 이름/시드 넣기
		for(VideoCall vc : list) {
			vc.setUserName(m.getUserName());
			vc.setNameSeed(m.getNameSeed());
		}
		
		//세션에 퐁당
		session.setAttribute("myVcRoom", list);
	}
	
	//초대받고 수락 대기중인 방 조회
	@Override
	public void myInvitedRoomList(HttpSession session) throws Exception{
		Member m = (Member)session.getAttribute("loginUser");
		List<VideoCall> list = dao.myInvitedRoomList(sqlSession, m);
		
		//세션에 퐁당
		session.setAttribute("invitedRoom", list);
	}
	
	//새로운 방 생성
	@Override
	@Transactional
	public int createRoom(HttpSession session, VideoCall vc) throws Exception{
		
		Member m = (Member)session.getAttribute("loginUser");
		
		//유효성 확인
		String maxParticipants = String.valueOf(vc.getMaxParticipants());
		String vcName = vc.getVcName();
		if(!maxParticipants.matches(Regexp.MAXPARTICIPANTS)
				|| !vcName.matches(Regexp.VCNAME)
				|| dao.countMyVcRoom(sqlSession,m)>5) return 0;
		
		vc.setUserNo(m.getUserNo());
		byte[] vcId = Regexp.createUUID();
		vc.setVcId(vcId);
		vc.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		
		// 관리자용 토큰 생성
		String ownerToken = dailyService.createMeetingToken(vcId, m, "owner").block();
		if(ownerToken==null) return 0;
		
		//daily.co에 방 생성
        String createdRoom = dailyService.createRoom(vcId, ownerToken);
        if(createdRoom==null) return 0;
        
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
					.roleType("M")
					.build();
			result = dao.insertOwner(sqlSession,vcm);
			
			//세션에 해당 방 정보 저장
			session.setAttribute("vcRoomUrl", createdRoom);
			session.setAttribute("vcToken", ownerToken);
			session.setAttribute("vcRoom", vc);
		}
		
		return result;
	}
	
	//기존 자신의 방 활성화
	@Transactional
	@Override
	public int recallRoom(HttpSession session, VideoCall vc) throws Exception{
		
		Member m = (Member)session.getAttribute("loginUser");
		
		//먼저 유효성 확인 - 해당하는 방이 존재하는지 함 봅시다.
		List<VideoCall> list = (List<VideoCall>)session.getAttribute("myVcRoom");
		if(list==null || list.isEmpty()) return 0;
		boolean flag = false;
		for(VideoCall vcFor : list) {
			//vcId로 방에 들어갈 거에요.
			if(Arrays.equals(vcFor.getVcId(), vc.getVcId())) {
				vc = vcFor;
				flag = true;
				break;
			}
		}
		if(!flag) return 0;

		//openvidu세션 생성
		
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
	
	//openvidu토큰 생성
	@Override
	public Map<String, Object> createToken(HttpSession session, String sessionId) throws Exception {
    	Map<String, Object> result = new HashMap<>();

        //openvidu 세션 조회
//        Session vcSession = openvidu.getActiveSession(sessionId);
//        if (vcSession == null) {
//            result.put("error", "세션이 존재하지 않습니다.");
//            return result;
//        }
        
        //여기 들어올 자격이 있는가?
        Member m = (Member)session.getAttribute("loginUser");
        VideoCall vc = (VideoCall)session.getAttribute("videoCallRoom");
		VcMember vcm = VcMember.builder()
				.vcId(vc.getVcId())
				.userNo(m.getUserNo())
				.build();
        String roleType = dao.haveLicense(sqlSession,vcm);

        
        //아니 이 새끼 DB에 없잖아. 너 잘 걸렸다 심심했는데
        if(roleType==null) {
        	result.put("error", "잘못된 접근입니다.");
    		return result;
    	}
        
        //roleType에 따른 openvidu권한 설정
//        OpenViduRole role;
//        switch(roleType) {
//        case "M": role = OpenViduRole.MODERATOR; break;
//        case "P": role = OpenViduRole.PUBLISHER; break;
//        case "S":
//        default : role = OpenViduRole.SUBSCRIBER;
//        }
//
//        //토큰 생성
//        ConnectionProperties props = new ConnectionProperties.Builder()
//                .type(ConnectionType.WEBRTC)
//                .role(role)
//                .data(m.getUserName()+":"+m.getNameSeed()) //data추출할 때는 뒤에서부터 : 찾기
//                .build();
//        Connection connection = vcSession.createConnection(props);

//        result.put("token", connection.getToken());
        
        return result;
	}
}
