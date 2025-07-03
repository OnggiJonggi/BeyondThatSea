package com.hugme.plz.common;

public class Regexp {
	//정규식 저장소
	public static final String USERID = "^[A-Za-z0-9]{1,30}$";
	public static final String USERPWD = "^[A-Za-z0-9]{4,20}$";
	public static final String USERNAME = "^.{1,30}$";
	
	public static final String VCNAME = "^.{1,30}$";
	public static final String MAXPARTICIPANTS = "^(10|[1-9])$";
	
	public static final int EXPIRETIME = 3600; //daily.co의 exp, 단위 : 초

}
