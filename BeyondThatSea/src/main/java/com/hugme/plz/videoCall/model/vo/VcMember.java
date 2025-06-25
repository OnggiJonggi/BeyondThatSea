package com.hugme.plz.videoCall.model.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VcMember {
	private String vcNo;
	private String userNo;
	private String status; //Y:접속, N:미접속
	private String roleType; //M:방장, P:관리자, S:일반
	//PUBLISHER,SUBSCRIBER,MODERATOR
	
	//VIDEO_CALL join
	private byte[] vcId;
}