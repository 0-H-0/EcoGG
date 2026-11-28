package semi.project.member.model.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import semi.project.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	// 로그인 회원 정보 조회
	Member login(Member member);

	// 아이디 및 닉네임 중복 확인
	int checkValue(HashMap<String, String> map);

	// 신규 회원 정보 삽입
	int insertMember(Member member);
	
	// 관리자 페이지 전채 조회수 조회에요 뿌릿!
	int selectMemberCount();

}
