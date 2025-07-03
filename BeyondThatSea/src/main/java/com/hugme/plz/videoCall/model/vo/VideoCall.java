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
	
	/*
	 * 프론트에서는 vcIdStr(하이픈 포함 문자열) 사용
	 * 백엔드에서 vcId(하이픈 미포함 바이트 배열) 사용
	 */
	private String vcIdStr;
	
	//방장 이름과 시드
	private String userName; //member
	private byte[] nameSeed; //member
	
	private String nameSeedStr; //nameSeed 문자열 변경
	
	//자신의 관리 권한
	private String roleTye; //vc_member
	
}
