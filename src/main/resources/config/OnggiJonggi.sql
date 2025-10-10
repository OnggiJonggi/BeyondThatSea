----------------시스템계정--------------------
--DROP USER BEYOND CASCADE;--계정 삭제
--
--create user BEYOND identified by BEYOND;
--grant resource, connect to BEYOND;
----------------------------------------------

--시퀀스
CREATE SEQUENCE SEQ_MEMBER
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE SEQ_CALL_ROOM
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE TABLE MEMBER (
    user_no      NUMBER           PRIMARY KEY,
    user_id      VARCHAR2(100) 	  UNIQUE,
    password     VARCHAR2(100)
);
COMMENT ON TABLE MEMBER IS '회원';
COMMENT ON COLUMN MEMBER.user_no IS '식별번호';
COMMENT ON COLUMN MEMBER.user_id IS '아이디';
COMMENT ON COLUMN MEMBER.password IS '비번';

CREATE TABLE FRIEND (
    user_no_1 NUMBER NOT NULL,
    user_no_2 NUMBER NOT NULL,

    CONSTRAINT pk_friend PRIMARY KEY (user_no_1, user_no_2),
    CONSTRAINT fk_user_no_1 FOREIGN KEY (user_no_1) REFERENCES member(user_no),
    CONSTRAINT fk_user_no_2 FOREIGN KEY (user_no_2) REFERENCES member(user_no)
);

COMMENT ON TABLE FRIEND IS '칭구칭긔';
COMMENT ON COLUMN FRIEND.user_no_1 IS '칭구';
COMMENT ON COLUMN FRIEND.user_no_2 IS '칭긔';

CREATE TABLE ROOM (
    room_no      NUMBER           PRIMARY KEY,
    room_name    VARCHAR2(100)
);
COMMENT ON TABLE ROOM IS '방';
COMMENT ON COLUMN ROOM.room_no IS '식별번호';
COMMENT ON COLUMN ROOM.room_name IS '이름';

CREATE TABLE ROOM_MEMBER (
    room_no      NUMBER,
    user_no      NUMBER
);
COMMENT ON TABLE ROOM_MEMBER IS '방 참여인원';
COMMENT ON COLUMN ROOM_MEMBER.room_no IS '방 ';
COMMENT ON COLUMN ROOM_MEMBER.user_no IS '이름';



commit;
