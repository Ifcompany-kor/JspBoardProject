# JspBoardProject
다이나믹 웹프로젝트를 이용하여서 모델2와 MVC디자인 패턴을 적용한 회원관리,게시판 프로젝트 (사진 업로드 기능 추가)

CREATE TABLE t_Member 
(
  id VARCHAR2(10 BYTE) PRIMARY KEY 
, pwd VARCHAR2(10 BYTE) NOT NULL 
, name VARCHAR2(50 BYTE) NOT NULL 
, email VARCHAR2(50 BYTE) NOT NULL 
, joindate DATE DEFAULT sysdate NOT NULL 
);

create table t_Board(
    articleNO number(10) PRIMARY KEY,
    parentNO number(10) default 0,
    title varchar2(500) not null,
    content varchar2(4000),
    imageFileName varchar2(100),
    writedate date default sysdate not null,
    id varchar2(10),
    CONSTRAINT FK_ID FOREIGN KEY(id)
    REFERENCES t_member(id)
);

사용한 DB는 오라클
