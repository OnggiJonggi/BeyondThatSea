package com.hugme.plz.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	//메인으로
	@RequestMapping("/")
	public String goMain() {
		return"main";
	}
}
