package com.docmall.basic.admin.membersinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.docmall.basic.common.dto.Criteria;
import com.docmall.basic.user.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembersService {

	private final MembersMapper membersMapper;
	
	// 페이징 & 검색조건이 추가된 리스트
	public List<UserVo> member_list(Criteria cri) {
		return membersMapper.member_list(cri);
	}
		
		// 데이터 총개수
	public int getTotalCount(Criteria cri) {
		return membersMapper.getTotalCount(cri);
	}
	
	// 회원(강제)탈퇴
	public void member_delete(String user_id) {
		membersMapper.member_delete(user_id);
	}
	
	// 메일정보 DB저장하기
	public void mem_mail_save(MembersVO vo) {
		membersMapper.mem_mail_save(vo);
	}
	
	// 수신등록
	public void save_recipients(ReceiverVO r_vo) {
		membersMapper.save_recipients(r_vo);
	}
	
	// 기존수신자 삭제
	public void deleteRecipientByEmail(String email) {
		membersMapper.deleteRecipientByEmail(email);
	}
	
	// 이메일발송시 수신자정보 삭제
	public void deletedbRecipientbyEmail(List<String> email) {
		for(String emails : email) {
			membersMapper.deletedbRecipientbyEmail(emails);
		}
	}
	
	
	// 등록된수신자목록
	public List<ReceiverVO> getReceiverList(List<String> emailList) {
        // 이메일 목록을 쉼표로 구분된 문자열로 변환
        String emailStr = String.join(",", emailList);
        
        // 쉼표 구분 문자열을 splitList라는 이름으로 쿼리에 전달
        return membersMapper.getReceiverList(emailStr);
    }
	
	// 메일발송리스트
	public List<MembersVO> mem_mail_list(Criteria cri,String mtitle) {
		return membersMapper.mem_mail_list(cri, mtitle);
	}
		
	// 메일발송리스트 개수
	public int getMailListCount(Criteria cri,String mtitle) {
		return membersMapper.getMailListCount(cri,mtitle);
	}
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	public String[] getALLMemberMail() {
		return membersMapper.getALLMemberMail();
	}
	
	// 메일발송 횟수증가
	public void mailSendCountUpdate(int idx) {
		membersMapper.mailSendCountUpdate(idx);
	}
	
	// 메일목록에서 메일발송
	public MembersVO getMailModifyinfo(int idx) {
		return membersMapper.getMailModifyinfo(idx);
	}
	
	// 메일수정
	public void mailedit(MembersVO vo) {
		membersMapper.mailedit(vo);
	}
	
	// 메일삭제
	public void maildelete(int idx) {
		membersMapper.maildelete(idx);
	}
	
	// 체크된메일삭제
	public void deltetcheckmail(Long idx) {
		membersMapper.deltetcheckmail(idx);
	}
	
}
