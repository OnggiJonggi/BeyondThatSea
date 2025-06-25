package com.hugme.plz.videoCall.model.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hugme.plz.common.Regexp;
import com.hugme.plz.member.model.vo.Member;
import com.hugme.plz.videoCall.model.dao.VideoCallDao;
import com.hugme.plz.videoCall.model.vo.VcMember;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;
import jakarta.servlet.http.HttpSession;



@Service
public class VideoCallServiceImpl implements VideoCallService{
	@Autowired
	private SqlSessionTemplate sqlSession;
    @Autowired
    private OpenVidu openvidu;
	@Autowired
	private VideoCallDao dao;
	
	
	//페이지 접근시 자신의 방 리스트를 보여준다.
	@Override
	public List<VideoCall> myRoomList(HttpSession session) {
		Member m = (Member)session.getAttribute("loginUser");
		List<VideoCall> list = dao.myRoomList(sqlSession, m);
		
		//방 리스트에 이름/시드 넣기
		for(VideoCall vc : list) {
			vc.setUserName(m.getUserName());
			vc.setNameSeed(m.getNameSeed());
		}
		
		//세션에 퐁당
		session.setAttribute("myVcRoom", list);
		
		return list;
	}
	
	
	//새로운 방 생성
	@Override
	@Transactional(rollbackFor = {OpenViduJavaClientException.class, OpenViduHttpException.class})
	public int createRoom(HttpSession session, VideoCall vc) throws Exception{
		
		Member m = (Member)session.getAttribute("loginUser");
		
		//유효성 확인
		String maxParticipants = String.valueOf(vc.getMaxParticipants());
		String vcName = vc.getVcName();
		if(!maxParticipants.matches(Regexp.MAXPARTICIPANTS)
				|| !vcName.matches(Regexp.VCNAME)
				|| dao.countMyVcRoom(sqlSession,m)>5) return 0;
		
		vc.setUserNo(m.getUserNo());
		vc.setVcId(Regexp.createUUID());
		
		//openvidu세션 생성
		SessionProperties properties = new SessionProperties.Builder().build();
		Session vcSession = openvidu.createSession(properties);
		String sessionId = vcSession.getSessionId();
		vc.setVcSession(sessionId);
		
		//데이터 모음집 넣기
		vc.setCurrParticipants(1);
		vc.setUserName(m.getUserName());
		vc.setNameSeed(m.getNameSeed());
		
		//db에 방 생성하고 참여자 목록에 추가
		int result = dao.createRoom(sqlSession,vc);
		if(result>0) {
			VcMember vcm = new VcMember(vc.getVcNo(),m.getUserNo(),"Y","M");
			result = dao.insertParticipate(sqlSession,vcm);
			
			//세션에 해당 방 정보 저장
			session.setAttribute("videoCallRoom", vc);
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
			//vcId와 createTimestamp로 방에 들어갈 거에요.
			if(Arrays.equals(vcFor.getVcId(), vc.getVcId())
					|| vcFor.getCreateTimestamp().equals(vc.getCreateTimestamp())) {
				vc = vcFor;
				flag = true;
				break;
			}
		}
		if(!flag) return 0;

		//openvidu세션 생성
		SessionProperties properties = new SessionProperties.Builder().build();
		Session vcSession = openvidu.createSession(properties);
		String sessionId = vcSession.getSessionId();
		vc.setVcSession(sessionId);
		
		//db에 저장하지 않는 프론트용 데이터
		vc.setCurrParticipants(1);
		vc.setUserName(m.getUserName());
		vc.setNameSeed(m.getNameSeed());
		
		//db에 방 세션 갱신하고 참여자 목록 갱신
		int result = dao.updateRoom(sqlSession,vc);
		if(result>0) {
			VcMember vcm = new VcMember(vc.getVcNo(),m.getUserNo(),"Y","M");
			result = dao.updateParticipate(sqlSession,vcm);
			
			//세션에 해당 방 정보 저장
			session.setAttribute("videoCallRoom", vc);
		}
		
		return result;
	}
	
	//openvidu토큰 생성
	@Override
	public Map<String, Object> createToken(HttpSession session, String sessionId) throws Exception {
    	Map<String, Object> result = new HashMap<>();

        //openvidu 세션 조회
        Session vcSession = openvidu.getActiveSession(sessionId);
        if (vcSession == null) {
            result.put("error", "세션이 존재하지 않습니다.");
            return result;
        }
        
        //여기 들어올 자격이 있는가?
        Member m = (Member)session.getAttribute("loginUser");
        String license = dao.haveLicense(sqlSession,m);
        
        //아니 이 새끼 DB에 없잖아. 너 잘 걸렸다 심심했는데
        if(license==null) {
            result.put("error", "잘못된 접근입니다.");
            return result;
        }
        
        //roleType에 따른 openvidu권한 설정
        OpenViduRole role;
        switch(license) {
        case "M": role = OpenViduRole.MODERATOR; break;
        case "P": role = OpenViduRole.PUBLISHER; break;
        case "S":
        default : role = OpenViduRole.SUBSCRIBER;
        }

        //토큰 생성
        ConnectionProperties props = new ConnectionProperties.Builder()
                .type(ConnectionType.WEBRTC)
                .role(role)
                .data(m.getUserName()+":"+m.getNameSeed()) //data추출할 때는 뒤에서부터 : 찾기
                .build();
        Connection connection = vcSession.createConnection(props);

        result.put("token", connection.getToken());
        
        return result;
	}
}
