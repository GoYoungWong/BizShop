package com.docmall.basic.admin.membersinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.user.UserVo;

public interface MembersMapper {

	// 페이징 & 검색조건이 추가된 리스트
	List<UserVo> member_list(Criteria cri);
	
	// 데이터 총개수
	int getTotalCount(Criteria cri);
	
	// 회원(강제)탈퇴
	void member_delete(String user_id);
	
	// 메일정보 DB저장하기
	void mem_mail_save(MembersVO vo);
	
	// 수신자등록
	void save_recipients(ReceiverVO r_vo);
	
	// 기존수신자 삭제
	void deleteRecipientByEmail(String email);
	
	// 이메일발송시 수신자정보 삭제
	void deletedbRecipientbyEmail(String email);
	
	// 등록된수신자목록
	List<ReceiverVO> getReceiverList(String email);
	
	// 메일발송리스트
	List<MembersVO> mem_mail_list(@Param("cri") Criteria cri,@Param("mtitle") String mtitle);
	
	// 메일발송리스트 개수
	int getMailListCount(@Param("cri") Criteria cri,@Param("mtitle") String mtitle);
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	String[] getALLMemberMail();
	
	// 메일발송 횟수증가
	void mailSendCountUpdate(int idx);
	
	// 메일목록에서 메일발송
	MembersVO getMailModifyinfo(int idx);
	
	// 메일수정
	void mailedit(MembersVO vo);
	
	// 메일삭제
	void maildelete(int idx);
	
	// 체크된메일삭제
	void deltetcheckmail(Long idx);
	
}
