package sea.that.beyond.memeber.model.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.dao.FriendDao;
import sea.that.beyond.memeber.model.vo.FriendIn;

public class FriendServiceImpl implements FriendService{
	@Autowired
	private SqlSessionTemplate sqlSession;
	@Autowired
	private FriendDao dao;
	
	
	/**
	 * 칭구칭긔 찾아주기
	 * @param session
	 * @return ResponseEntity<List<FriendOut>>
	 */
	@Override
	public ResponseEntity<List<String>> myFriendList(HttpSession session) throws Exception {
		String userId = (String)session.getAttribute("loginUser");
		
		List<String> result = dao.myFriendList(sqlSession, userId);
		
		return ResponseEntity.ok(result);
	}


	/**
	 * 넌 이제 내 칭구 아니야.
	 * @param 세션
	 * @return 읎다
	 */
	@Override
	public void delete(HttpSession session, String friendId) throws Exception {
		String userId = (String)session.getAttribute("loginUser");
		FriendIn friendIn = FriendIn.builder()
				.id1(userId)
				.id2(friendId).build();
		int result = dao.delete(sqlSession, friendIn);
		if(!(result>0)) throw new Exception("칭구 삭제 실패!");
	}


	/**
	 * 넌 이제 내 칭구야!
	 * @param 세션, friendId
	 * @return 읎어
	 */
	@Override
	public void insert(HttpSession session, String friendId) throws Exception {
		String userId = (String)session.getAttribute("loginUser");
		FriendIn friendIn = FriendIn.builder()
				.id1(userId)
				.id2(friendId).build();
		int result = dao.insert(sqlSession, friendIn);
		if(!(result>0)) throw new Exception("칭구 추가 실패!");
	}
}
