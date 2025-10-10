package sea.that.beyond.memeber.model.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;

public interface FriendService {

	ResponseEntity<List<String>> myFriendList(HttpSession session) throws Exception;

	void delete(HttpSession session, String friendId) throws Exception;

	void insert(HttpSession session, String friendId) throws Exception;

}
