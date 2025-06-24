package com.hugme.plz.common;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.stereotype.Component;


@Component
public class Regexp {
	//정규식 저장소
	public static final String USERID = "^[A-Za-z0-9]{1,30}$";
	public static final String USERPWD = "^[A-Za-z0-9]{4,20}$";
	public static final String USERNAME = "^.{1,30}$";
	public static final String VCNAME = "^.{1,30}$";
	public static final String MAXPARTICIPANTS = "^(10|[1-9])$";
	
	
	//uuid생성기
	public static byte[] createUUID() {
	    UUID uuid = UUID.randomUUID();
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    return bb.array();
	}
}
