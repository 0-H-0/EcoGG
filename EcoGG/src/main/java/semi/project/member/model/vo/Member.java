package semi.project.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {
	private int memberNo;			// 회원 번호
	private String memberName;		// 회원 이름
	private String memberNickName;	// 닉네임
	private String memberId;		// 아이디
	private String memberPwd;		// 비밀번호
	private String memberEmail;	// 이메일
	private String memberStatus;	// 회원 상태 (ACTIVE, INACTIVE, SUSPENDED)
	private String separateMem;	// 관리자(Y) 및 회원 등급(1~5)
	private Date enrollDate;		// 가입일
	private Date updateDate;		// 회원 정보 수정일
}
