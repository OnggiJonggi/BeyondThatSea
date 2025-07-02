package com.hugme.plz.common;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Regexp {
	//정규식 저장소
	public static final String USERID = "^[A-Za-z0-9]{1,30}$";
	public static final String USERPWD = "^[A-Za-z0-9]{4,20}$";
	public static final String USERNAME = "^.{1,30}$";
	
	public static final String VCNAME = "^.{1,30}$";
	public static final String MAXPARTICIPANTS = "^(10|[1-9])$";
	
	public static final int EXPIRETIME = 3600; //daily.co의 exp, 단위 : 초
	
	
	//uuid생성기
	public static Map<String, Object> createUUID() {
	    UUID uuid = UUID.randomUUID();
	    
	    // 문자열 형식 UUID (하이픈 포함)
	    String uuidStr = uuid.toString();
	    
	    // 바이트 배열 형식 UUID
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    byte[] uuidRaw = bb.array();
	    
	    // 결과 맵 생성
	    Map<String, Object> result = new HashMap<>();
	    result.put("uuid", uuidStr);
	    result.put("uuidRaw", uuidRaw);
	    
	    return result;
	}
}
