package sea.that.beyond.memeber.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import sea.that.beyond.memeber.model.vo.UserIn;
import sea.that.beyond.memeber.model.vo.UserOut;

@Repository
public class MemberDao {

	public int signup(SqlSessionTemplate sqlSession, UserIn signup) {
		return sqlSession.insert("memberMapper.insertMember", signup);
	}

	public int login(SqlSessionTemplate sqlSession, UserOut userOut) {
		return sqlSession.selectOne("memberMapper.selectMember", userOut);
	}

}
