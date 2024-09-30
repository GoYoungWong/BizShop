package com.docmall.basic.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.qnaboard.QnaBoardVO;

public interface UserMapper {

	// 회원가입
	void join(UserVo vo);
	
	// 아이디 중복체크
	String idCheck(String user_id);
	
	// 로그인
	UserVo login(String user_id);
	
	// 아이디찾기
	String idfind(@Param("user_name") String user_name, @Param("user_email") String user_email);
	
	// 임시비밀번호
	String pwfind(@Param("user_id")String user_id,@Param("user_name") String user_name,@Param("user_email") String user_email);
	
	// 임시비밀번호업데이트
	void tempPwUpdate(@Param("user_id")String user_id,@Param("temp_enc_pw") String temp_enc_pw);

	// 회원탈퇴
	void delete(String user_id);
	
	// 내정보수정
	void modify(UserVo vo);
	
	// 비밀번호 변경
	void changePw(@Param("user_id") String user_id,@Param("new_user_pwd") String new_user_pwd);
	
	// 어느 api로 로그인한지 확인
	String existsUserInfo(String sns_email);
	
	// sns user 중복체크
	String sns_user_check(String sns_email);
	
	// 입력값 삽입
	void sns_user_insert(SNSUserDto dto);
	
	// 사용자 QnA리스트
	List<QnaBoardVO> userqnalist(@Param("user_id") String user_id, @Param("cri") Criteria cri);

	// 사용자 ID로 QnA 개수를 가져오는 쿼리
    int getCountQnaByUserId(String user_id);
    
    // 답변보기
    QnaBoardVO myqna_form(Long qna_idx);
    
    // 내 qna지우기
    void myqna_delete(Long qna_idx);
	
}
