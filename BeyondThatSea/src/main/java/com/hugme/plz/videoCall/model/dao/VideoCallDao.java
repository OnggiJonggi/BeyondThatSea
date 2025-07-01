package com.hugme.plz.videoCall.model.dao;

import java.util.List;
import java.util.Map;

import com.hugme.plz.member.model.vo.Member;
import com.hugme.plz.videoCall.model.vo.VcMember;
import com.hugme.plz.videoCall.model.vo.VideoCall;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class VideoCallDao {

	public int createRoom(SqlSessionTemplate sqlSession, VideoCall vc) {
		return sqlSession.insert("videoCallMapper.createRoom",vc);
	}

	public int countMyVcRoom(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectOne("videoCallMapper.countMyVcRoom",m);
	}
	
	public List<VideoCall> myRoomList(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectList("videoCallMapper.myRoomList",m);
	}

	public List<VideoCall> myInvitedRoomList(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectList("videoCallMapper.myInvitedRoomList",m);
	}
	
	public int insertOwner(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.insert("videoCallMapper.insertOwner",vcm);
	}

	public int updateParticipate(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.update("videoCallMapper.updateParticipate",vcm);
	}
	
	public String haveLicense(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.selectOne("videoCallMapper.haveLicense",vcm);
	}

	public int updateRoom(SqlSessionTemplate sqlSession, VideoCall vc) {
		return sqlSession.update("videoCallMapper.updateRoom",vc);
	}

	public int goInvitedRoom(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.update("videoCallMapper.goInvitedRoom",vcm);
	}

	public VideoCall selectRoom(SqlSessionTemplate sqlSession, VideoCall vc) {
		return sqlSession.selectOne("videoCallMapper.selectRoom",vc);
	}

	public int insertSubscriber(SqlSessionTemplate sqlSession, Map<String, Object> map) {
		return sqlSession.insert("videoCallMapper.insertSubscriber", map);
	}



}
