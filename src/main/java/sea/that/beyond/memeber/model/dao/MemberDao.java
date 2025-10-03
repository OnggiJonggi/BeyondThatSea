package sea.that.beyond.memeber.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import sea.that.beyond.memeber.model.vo.Signup;

@Repository
public class MemberDao {

	public int signup(SqlSessionTemplate sqlSession, Signup signup) {
		return sqlSession.insert("memberMapper.insertMember", signup);
	}

	public int login(SqlSessionTemplate sqlSession, Signup signup) {
		return sqlSession.selectOne("memberMapper.selectMember", signup);
	}

}
