package sea.that.beyond.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sea.that.beyond.room.model.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {
	@Autowired RoomService service;
	
	@ResponseBody
	@PostMapping("/myRoomList")
	public ResponseEntity<List<RoomOut>> myRoomList() {
		
		return "room/myroom";
	}
}
