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
	private int memberNo;
	private String memberName;
	private String memberNickName;
	private String memberId;
	private String memberPwd;
	private String memberEmail;
	private String memberStatus;
	private String separateMem;
	private Date enrollDate;
	private Date updateDate;
}
