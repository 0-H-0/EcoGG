package semi.project.member.exception;

public class MemberException extends RuntimeException {
	private String type; // 회원 관련 오류 유형(ENROLL, UPDATE)
	
	// 기본생성자
	public MemberException() {}
	
	// 오류 메시지만 전달받는 생성자
	public MemberException(String msg) {
		super(msg);
	}
	
	// 오류 메시지와 오류 유형을 전달받는 생성자
	public MemberException(String msg, String type) {
		super(msg);
		this.type = type;
	}
	
	// 오류 유형 반환
	public String getType () {
		return type;
	}
}
