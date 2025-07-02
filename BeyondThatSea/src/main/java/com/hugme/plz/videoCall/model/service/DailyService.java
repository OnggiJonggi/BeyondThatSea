package com.hugme.plz.videoCall.model.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.hugme.plz.common.Regexp;
import com.hugme.plz.member.model.vo.Member;
import com.hugme.plz.videoCall.model.vo.VideoCall;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final WebClient webClient;
    
    //토큰 생성
    public Mono<String> createMeetingToken(String uuidStr, Member m, String roleType) throws Exception {
    	//호출한 유저가 주인장일 경우
    	boolean isOwner = "owner".equals(roleType);
    	
    	//요청 몸매 만들기
    	Map<String, Object> requestBody = Map.of(
    		    "properties", Map.of(
    		        "room_name", uuidStr,
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
    public String createRoom(String uuidStr, VideoCall vc) {
    	
    	//현재 시간 추출
    	long currentTime = System.currentTimeMillis();
		vc.setCreateTimestamp(new Timestamp(currentTime));

    	//요청 몸매 만들기
        Map<String, Object> requestBody = Map.of(
            "name", uuidStr,
            "privacy", "private",
            "properties", Map.of(
                "exp", (int)(currentTime/1000 + Regexp.EXPIRETIME),
                "enable_screenshare", true,
                "enable_chat", true,
                "lang", "ko",
                "max_participants", vc.getMaxParticipants()
            )
        );
        
        
        String url = webClient.post()
            .uri("/rooms")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .map(response -> (String) response.get("url")).block();
        
        return url;
    }
    

}
