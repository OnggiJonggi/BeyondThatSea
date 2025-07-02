package com.hugme.plz.videoCall.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VcMember {
	private byte[] vcId;
	private String userNo;
	private String status; //상태(Y:접속, N:미접속, U:수락 대기중)
	private String roleType; //관리 권한(O:생성자, A:관리자, S:관전자)
}