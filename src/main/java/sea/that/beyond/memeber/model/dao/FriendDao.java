package sea.that.beyond.memeber.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import sea.that.beyond.memeber.model.vo.FriendIn;

@Repository
public class FriendDao {

	public List<String> myFriendList(SqlSessionTemplate sqlSession, String userId) {
		return sqlSession.selectList("friendMapper.myFriendList",userId);
	}

	public int delete(SqlSessionTemplate sqlSession, FriendIn friendIn) {
		return sqlSession.delete("friendMapper.delete", friendIn);
	}
	
	public int insert(SqlSessionTemplate sqlSession, FriendIn friendIn) {
		return sqlSession.insert("friendMapper.insert", friendIn);
	}

}
