package com.hugme.plz.videoCall.model.dao;

import java.util.List;

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
		return sqlSession.selectOne("videoCallMapper.countMyVcRoomList",m);
	}
	
	public List<VideoCall> myRoomList(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectList("videoCallMapper.myRoomList",m);
	}

	public int insertParticipate(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.insert("videoCallMapper.insertParticipate",vcm);
	}

	public int updateParticipate(SqlSessionTemplate sqlSession, VcMember vcm) {
		return sqlSession.update("videoCallMapper.updateParticipate",vcm);
	}
	
	public String haveLicense(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectOne("videoCallMapper.haveLicense",m);
	}

	public int updateRoom(SqlSessionTemplate sqlSession, VideoCall vc) {
		return sqlSession.update("videoCallMapper.updateRoom",vc);
	}


}
