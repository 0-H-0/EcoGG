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
	
	public Member login(Member member) {
		return mapper.login(member);
	}

	public int checkValue(HashMap<String, String> map) {
		return mapper.checkValue(map);
	}

	public int insertMember(Member member) {
		return mapper.insertMember(member);
	}

}
