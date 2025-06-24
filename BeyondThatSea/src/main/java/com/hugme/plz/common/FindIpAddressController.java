package com.hugme.plz.common;

import java.net.InetAddress;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FindIpAddressController {
	
	
	//서버 내부ip주소 확인
	//192.168.150.27 &gt;-이 주소가 내부ip로 잡히는지 확인해!!
	@ResponseBody
	@PostMapping("findServerPrivateIP")
	public String findServerPrivateIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
