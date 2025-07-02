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
public class VideoCall {
	private byte[] vcId;
	private String userNo; //방장 회원번호
	private String vcName;
	private Timestamp createTimestamp;
	private int maxParticipants;
	private String status;
	
	//방장 이름과 시드
	private String userName; //member
	private byte[] nameSeed; //member
	
	//자신의 관리 권한
	private String roleTye; //vc_member
	
}
