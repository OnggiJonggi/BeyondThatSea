package com.hugme.plz.common;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UuidUtil {
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
	
	//byte[] to string : UUID 바이트를 하이픈 포함 문자열로 변경하기
	public static String byteArrToStr(byte[] uuidBytes) throws Exception{
        ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        UUID uuid = new UUID(mostSigBits, leastSigBits);
        return uuid.toString();
	}
	
	//string to byte[] : UUID 하이픈 포함 문자열을 바이트로 변경하기
    public static byte[] strToByteArr(String uuidStr) throws Exception{
        UUID uuid = UUID.fromString(uuidStr);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
