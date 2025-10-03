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
    user_id      VARCHAR2(100),
    password     VARCHAR2(100)
);
COMMENT ON TABLE MEMBER IS '회원';
COMMENT ON COLUMN MEMBER.user_no IS '식별번호';
COMMENT ON COLUMN MEMBER.user_id IS '아이디';
COMMENT ON COLUMN MEMBER.password IS '비번';



commit;
