package com.hugme.plz.member.model.dao;

import com.hugme.plz.member.model.vo.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

	public int checkUserId(SqlSessionTemplate sqlSession, String userId) {
		return sqlSession.selectOne("memberMapper.checkUserId", userId);
	}

	public Member login(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.selectOne("memberMapper.login", m);
	}

	public int enroll(SqlSessionTemplate sqlSession, Member m) {
		return sqlSession.insert("memberMapper.enroll",m);
	}

}
