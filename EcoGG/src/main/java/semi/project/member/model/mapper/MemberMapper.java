package semi.project.member.model.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import semi.project.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	Member login(Member member);

	int checkValue(HashMap<String, String> map);

	int insertMember(Member member);
	
	// 관리자 페이지 전채 조회수 조회에요 뿌릿!
	int selectMemberCount();

}
