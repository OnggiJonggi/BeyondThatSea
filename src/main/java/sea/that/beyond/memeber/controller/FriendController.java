package sea.that.beyond.memeber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import sea.that.beyond.memeber.model.service.FriendService;
import sea.that.beyond.memeber.model.vo.FriendOut;

@Controller
@RequestMapping("/friend")
public class FriendController {
	@Autowired
	private FriendService service;
	
	
	@ResponseBody
	@PostMapping("myFriendList")
	public ResponseEntity<List<String>> myFriendList(HttpSession session){
		try {
			return service.myFriendList(session); 
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).build();
		}
	}
	
	@ResponseBody
	@PostMapping("delete")
	public void delete(HttpSession session, String friendId) {
		try {
			service.delete(session, friendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@PostMapping("insert")
	public void insert(HttpSession session, String friendId) {
		try {
			service.insert(session, friendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
