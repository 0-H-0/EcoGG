package semi.project.member.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import semi.project.member.model.mapper.MemberMapper;
import semi.project.member.model.vo.Member;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper mapper;
	
	// 로그인 회원 정보 조회
	public Member login(Member member) {
		return mapper.login(member);
	}
	
	// 아이디 및 닉네임 중복 확인
	public int checkValue(HashMap<String, String> map) {
		return mapper.checkValue(map);
	}
	
	// 신규 회원 등록
	public int insertMember(Member member) {
		return mapper.insertMember(member);
	}
	
	// 관리페이지 전체 회원 수 조회에용 뿌릿!!
	public int selectMemberCount() {
		return mapper.selectMemberCount();
	}

}
