package com.docmall.basic.user;

import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.qnaboard.QnaBoardVO;

import lombok.RequiredArgsConstructor;

// 구현클래스 : 인터페이스를 상속받는 클래스
@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserMapper userMapper;

	// 회원가입
	public void join(UserVo vo) {
		userMapper.join(vo);
		
	}
	// 아이디 중복체크
	public String idCheck(String user_id) {
		// TODO Auto-generated method stub
		return userMapper.idCheck(user_id);
	}
	
	// 로그인
	public UserVo login(String user_id) {
		return userMapper.login(user_id);
	}
	
	// 아이디찾기
	public String idfind(String user_name,String user_email) {
		return userMapper.idfind(user_name,user_email);
	}
	
	// 임시비밀번호
	public String pwfind(String user_id,String user_name,String user_email) {
		return userMapper.pwfind(user_id, user_name,user_email);
	}
	
	// 임시비밀번호업데이트
	public void tempPwUpdate(String user_id,String temp_enc_pw) {
		userMapper.tempPwUpdate(user_id, temp_enc_pw);
	}
	
	// 회원탈퇴
	public void delete(String user_id) {
		userMapper.delete(user_id);
	}
	
	// 내정보수정
	public void modify(UserVo vo) {
		userMapper.modify(vo);
	}
	
	// 비밀번호 변경
		void changePw(String user_id,String new_user_pwd) {
			userMapper.changePw(user_id, new_user_pwd);
		}
	
	// 어느 api로 로그인한지 확인
	public String existsUserInfo(String sns_email) {
		return userMapper.existsUserInfo(sns_email);
	}
		
	// sns user 중복체크
	public String sns_user_check(String sns_email) {
		return userMapper.sns_user_check(sns_email);
	}
		
	// 입력값 삽입
	public void sns_user_insert(SNSUserDto dto) {
		userMapper.sns_user_insert(dto);
	}
	
	// 사용자 QnA리스트
	public List<QnaBoardVO> userqnalist(@Param("user_id") String user_id, @Param("cri") Criteria cri) {
		return userMapper.userqnalist(user_id, cri);
	}

	// 사용자 ID로 QnA 개수를 가져오는 쿼리
    public int getCountQnaByUserId(String user_id) {
    	return userMapper.getCountQnaByUserId(user_id);
    }
    
    // 답변보기
    public QnaBoardVO myqna_form(Long qna_idx) {
    	return userMapper.myqna_form(qna_idx);
    }
    
    // 내 qna지우기
    public void myqna_delete(Long qna_idx) {
    	userMapper.myqna_delete(qna_idx);
    }
    
	
	// 임시비밀번호 발급
	// 임시 비밀번호 생성(UUID 이용)
	// UUID를 이용하여 임시 비밀번호를 생성하고, 하이픈(-)을 제거하고, 10자리까지 비밀번호 생성
	public String getTempPw() {
		
	return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
	}
}	
