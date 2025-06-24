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
}
