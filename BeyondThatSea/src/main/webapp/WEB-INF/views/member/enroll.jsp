<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원가입</title>
</head>
<body>
	<%@include file="/WEB-INF/views/common/header.jsp"%>
	<h3>회원가입</h3>
	<form action="${pageScope.root }/member/enroll" id="insertForm"
		method='post' novalidate>
		<!-- 아이디 -->
		<div>
			<label for="userId"><b>* 아이디</b></label><br> <input type="text"
				id="userId" placeholder="아이디" name="userId" required>
			<div id="checkIdResult">아이디를 입력해주세요.</div>
			<!-- 추가된 요소 -->
		</div>

		<!-- 비밀번호 -->
		<div>
			<label for="userPwd"><b>* 비밀번호</b></label><br> <input
				type="password" id="userPwd" placeholder="비밀번호" name="userPwd"
				required>
		</div>

		<!-- 비밀번호 확인 -->
		<div>
			<label for="checkPwd"><b>* 비밀번호 확인</b></label><br> <input
				type="password" id="checkPwd" placeholder="비밀번호 재확인" required>
			<div id="checkPwdResult">비밀번호를 입력해주세요.</div>
			<!-- id 추가 -->
		</div>

		<!-- 별명 -->
		<div>
			<label for="userName"><b>* 별명</b></label><br> <input type="text"
				id="userName" placeholder="별명" name="userName" required>
			<div id="checkNameResult">이름을 입력해주세요.</div>
		</div>

		<!-- 최종 결과 메시지 -->
		<div id="finalResult">모든 필수 항목을 입력하세요</div>

		<!-- 제출 및 초기화 -->
		<div>
			<button type="submit" id="insertSubmit" disabled>가입하기</button>
			<button type="button" id="insertReset">초기화</button>
		</div>
	</form>

	<script type="text/javascript">
		//모든 검증이 true여야 sumbmit버튼 활성화됨
		let idPass = false;
		let pwdPass = false;
		let namePass = false;

		//아이디 검증
		document.getElementById("userId").addEventListener('input', function() {
			idPass = false;
			toggleSubmit();
			idConfirm();
		});
		function idConfirm() {
			const userId = document.getElementById("userId").value.trim();
			const regex = /^[A-Za-z0-9]{1,30}$/;
			const checkIdResult = document.getElementById('checkIdResult'); // 요소 참조 추가

			if (regex.test(userId)) {
				checkIdResult.textContent = userId;
				debounce();
			} else {
				checkIdResult.textContent = "특수문자 제외 30글자 이내로 입력해줘";
				idPass = false;
				toggleSubmit();
			}
		}

		let debounce = _.	debounce(function() {
			let userId = document.getElementById("userId").value.trim();
			$.ajax({
				type : "POST",
				url : "${root}/member/checkUserId",
				data : {
					userId : userId
				},
				success : function(result) {
					if (result == "pass") {
						document.getElementById("checkIdResult").innerHTML = "'"
							+ userId
							+ "'는 사용 가능한 아이디입니다.";
						idPass = true;
						toggleSubmit();
					} else if (result == 'noPass') {
						document.getElementById("checkIdResult").innerHTML = "'"
							+ userId
							+ "'라는 아이디는 사용중입니다.";
						idPass = false;
						toggleSubmit();
					} else {
						document.getElementById("checkIdResult").innerHTML = "서버 에러";
						idPass = false;
						toggleSubmit();
					}
				},
				error : function() {
					document.getElementById("checkIdResult").innerHTML = "통신 오류!";
					idPass = false;
					toggleSubmit();
				}
			});
		}, 300);

		document.getElementById('userPwd').addEventListener('input',function() {
			const pwd = this.value.trim();
			const regex = /^[A-Za-z0-9]{4,20}$/;
			const checkPwdResult = document.getElementById('checkPwdResult');

			if (regex.test(pwd)) {
				confirmPwd();
			} else {
				checkPwdResult.textContent = "특수문자 제외 4 ~ 20글자로 입력해줘";
				pwdPass = false;
				toggleSubmit();
			}
		});

		document.getElementById("checkPwd").addEventListener('input',
				confirmPwd);

		function confirmPwd() {
			const checkPwdResult = document.getElementById('checkPwdResult');
			const checkPwd = document.getElementById('checkPwd').value.trim();
			const userPwd = document.getElementById('userPwd').value.trim();

			if (checkPwd === "") {
				if (userPwd === "") {
					checkPwdResult.textContent = "비밀번호를 입력해주세요.";
				} else {
					checkPwdResult.textContent = "사용 가능한 비밀번호입니다.";
				}
				pwdPass = false;
			} else if (checkPwd === userPwd) {
				checkPwdResult.textContent = "합격!";
				pwdPass = true;
			} else {
				checkPwdResult.textContent = "비밀번호가 일치하지 않습니다";
				pwdPass = false;
			}
			toggleSubmit();
		}

		document.getElementById('userName').addEventListener('input',function() {
			let checkNameResult = document.getElementById("checkNameResult")
			if (this.value.trim().length > 30) {
				checkNameResult.innerHTML = "이름이 너무 길어요";
				namePass = false;
			} else if (this.value.trim().length < 1) {
				checkNameResult.innerHTML = "이름을 입력해 주세요";
				namePass = false;
			} else {
				checkNameResult.innerHTML = "좋아요!";
				namePass = true;
			}
			toggleSubmit();
		});

		document.getElementById("insertReset").addEventListener('click',function() {
			if (confirm("진짜?")) {
				document.getElementById("insertForm").reset();
				idPass = false;
				pwdPass = false;
				namePass = false;

				document.getElementById('checkIdResult').textContent = "아이디를 입력해주세요.";
				document.getElementById('checkPwdResult').textContent = "비밀번호를 입력해주세요.";
				document.getElementById('checkNameResult').textContent = "이름을 입력해주세요.";
				document.getElementById("finalResult").innerHTML = "모든 필수 항목을 입력하세요";
				toggleSubmit();
			}
		});

		let toggolePassed = false;
		function toggleSubmit() {
			if (idPass && pwdPass && namePass) {
				setButtonState('active');
				toggolePassed = true;
			} else if (toggolePassed == true) {
				setButtonState('disabled');
				toggolePassed = false;
			}
		};

		function setButtonState(state) {
			let insertSubmit = document.getElementById("insertSubmit");
			let checkfinalResult = document.getElementById("finalResult");

			switch (state) {
			case 'disabled':
				checkfinalResult.innerHTML = "모든 필수 항목을 확인해 주세요";
				insertSubmit.disabled = true;
				break;
			case 'active':
				checkfinalResult.innerHTML = "좋아요!";
				insertSubmit.disabled = false;
			}
		}
	</script>
</body>
</html>
