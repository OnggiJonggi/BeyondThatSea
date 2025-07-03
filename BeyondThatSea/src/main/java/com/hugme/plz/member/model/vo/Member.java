package com.hugme.plz.member.model.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
	private String userNo;
	private String userId;
	private String userPwd;
	private String userName;
	private byte[] nameSeed;
	private Timestamp enrollDate;
	private Timestamp modiofyDate;
	private String status;
	
	/*
	 * 프론트에서는 nameSeedStr(하이픈 포함 문자열) 사용
	 * 백엔드에서 nameSeed(하이픈 미포함 바이트 배열) 사용
	 */
	private String nameSeedStr;
}
