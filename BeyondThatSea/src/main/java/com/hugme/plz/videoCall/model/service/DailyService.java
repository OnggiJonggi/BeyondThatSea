package com.hugme.plz.videoCall.model.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hugme.plz.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final WebClient webClient;
    
    //토큰 생성
    public Mono<String> createMeetingToken(byte[] vcId, Member m, String roleType) throws Exception {
    	
    	//vcId를 String으로 변환
    	String vcIdStr = new String(vcId, StandardCharsets.UTF_8);
    	
    	boolean isOwner = "owner".equals(roleType);
    	
    	Map<String, Object> requestBody = Map.of(
    		    "properties", Map.of( // ✅ "properties" 필드 필수!
    		        "room_name", vcIdStr,
    		        "is_owner", isOwner,
    		        "user_name", m.getUserName(),
    		        "enable_screenshare", true,
    		        "exp", System.currentTimeMillis()/1000 + 86400
    		    )
    		);
    	
    	return webClient.post()
    			.uri("/meeting-tokens")
    			.bodyValue(requestBody)
    			.retrieve()
    			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
    			.map(response -> (String) response.get("token"));
    }
    
    //새로운 방 생성
    public String createRoom(byte[] vcId, String token) {
    	String vcIdStr = new String(vcId, StandardCharsets.UTF_8);
    	
        Map<String, Object> roomConfig = Map.of(
            "name", vcIdStr,
            "privacy", "private",
            "properties", Map.of(
                "exp", System.currentTimeMillis()/1000 + 86400,
                "enable_screenshare", true,
                "enable_chat", true
            )
        );
        
        
        String url = webClient.post()
            .uri("/rooms")
            .header("Authorization", "Bearer " + token)
            .bodyValue(roomConfig)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .map(response -> (String) response.get("url")).block();
        
        System.out.println("새로운 url이 생성되었어요! : " + url);
        return url;
    }
    

}
