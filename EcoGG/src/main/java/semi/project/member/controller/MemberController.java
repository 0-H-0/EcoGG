package semi.project.member.controller;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import semi.project.member.exception.MemberException;
import semi.project.member.model.vo.Member;
import semi.project.member.service.MemberService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService mService;
	private final BCryptPasswordEncoder bcrypt;

	// 로그인 페이지 이동
	@GetMapping("/login")
	public String loginView() {
		return "/views/member/login";
	}
	
	/**
	 * 회원 로그인 처리 및 회원 구분에 따른 페이지 이동
	 * @param member 로그인 시 입력한 회원 아이디와 비밀번호
	 * @param session 로그인 회원 정보를 저장할 세션
	 * @param model 로그인 실패 시 오류 메시지를 전달할 객체
	 * @return 로그인 결과에 따른 페이지 경로
	 */
	@PostMapping("/login")
	public String login(@ModelAttribute Member member, HttpSession session, Model model) {
		// 회원 정보 조회
		Member loginUser = mService.login(member);
		
		// 회원 정보 및 비밀번호 일치 여부 확인
		if(loginUser != null && bcrypt.matches(member.getMemberPwd(), loginUser.getMemberPwd())) {
			// 이용 정지 회원 확인
			if(!loginUser.getMemberStatus().equals("SUSPENDED")) {
				// 로그인 회원 정보 세션에 저장
				session.setAttribute("loginUser", loginUser);
				
				if(loginUser.getSeparateMem().equals("Y")) {
					return "redirect:/admin/home";
				} else
					return "redirect:/home";
			} else {
				return "views/member/login";
			}
		} else {
			// 로그인 실패 메시지 전달 및 로그인 페이지 반환
			model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
			return "views/member/login";
		}
	}
	
	// 회원가입 페이지 이동
	@GetMapping("/enroll")
	public String enrollView() {
		return "views/member/enroll";
	}
	
	/**
	 * 회원가입 정보 검증 및 신규 회원 등록
	 * @param member 회원가입 시 입력한 회원 정보
	 * @param emailId 이메일 아이디
	 * @param emailDomain 이메일 도메인
	 * @param session 이메일 인증 정보를 저장한 세션
	 * @return 회원가입 결과에 따른 페이지 경로
	 * @throws MemberException 회원 등록에 실패한 경우
	 */
	@PostMapping("/enroll")
	public String insertMember (@ModelAttribute Member member, @RequestParam("emailId") String emailId, @RequestParam("emailDomain") String emailDomain, HttpSession session) {
		// 회원가입 입력값 유효성 검사
		if(member.getMemberId() == null || member.getMemberId().isBlank() || member.getMemberId().trim().length() < 2) return "redirect:/member/enroll";
		if(member.getMemberPwd() == null || member.getMemberPwd().isBlank()) return "redirect:/member/enroll";
		if(!member.getMemberPwd().matches(".*[a-zA-Z].*") || !member.getMemberPwd().matches(".*[0-9].*") || !member.getMemberPwd().matches(".*[!?@#$%&*].*") || !member.getMemberPwd().matches("^[a-zA-Z0-9!?@#$%&*]{8,16}$")) return "redirect:/member/enroll";
		if(member.getMemberName() == null || member.getMemberName().isBlank()) return "redirect:/member/enroll";
		if(member.getMemberNickName() == null || member.getMemberNickName().isBlank() || member.getMemberNickName().trim().length() < 2) return "redirect:/member/enroll";
		if(emailId == null || emailId.trim().isBlank()) return "redirect:/member/enroll";
		if(!emailDomain.equals("naver.com") && !emailDomain.equals("gmail.com") && !emailDomain.equals("hanmail.net") && !emailDomain.equals("nate.com")) return "redirect:/member/enroll";
		
		// 이메일 주소 생성
		member.setMemberEmail(emailId + "@" + emailDomain);
		// 이메일 인증 여부 확인
		String verifyEmail = (String)session.getAttribute("verifyEmail");
		if(verifyEmail == null || !member.getMemberEmail().equals(verifyEmail)) return "redirect:/member/enroll";
		
		// 아이디, 닉네임, 이메일 중복 확인
		int count = 0;
		HashMap<String, String> map = new HashMap<>();
		String[] column = {"member_id", "member_nickname", "member_email"};
		String[] value = {member.getMemberId(), member.getMemberNickName(), member.getMemberEmail()};
		for(int i =0; i < column.length; i++) {
			map.put("column", column[i]);
			map.put("value", value[i]);
			count += mService.checkValue(map);
		}
		if(count != 0) return "redirect:/member/enroll";
		
		// 비밀번호 암호화
		member.setMemberPwd(bcrypt.encode(member.getMemberPwd()));
		
		// 신규 회원 등록
		int result = mService.insertMember(member);
		if(result > 0) {
			// 회원가입 성공 시 이메일 인증 관련 세션 정보 삭제
			session.removeAttribute("email");
			session.removeAttribute("random");
			session.removeAttribute("verifyEmail");
			session.removeAttribute("emailSentTime");
			return "redirect:/member/login";
		} else {
			throw new MemberException("회원가입에 실패했습니다.", "ENROLL");
		}
	}
	
	@GetMapping("/mypage")
	public String mypage() {
		return "views/member/mypage";
	}
	
	// 로그아웃
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
	
	/**
	 * 회원 관련 예외 처리 및 오류 안내 페이지 이동
	 * @param e 발생한 회원 관련 예외 객체
	 * @param model 오류 메시지를 전달할 객체
	 * @return 회원 전용 오류 안내 페이지 경로
	 */
	@ExceptionHandler(MemberException.class)
	public String error(MemberException e, Model model) {
		// 예외 메시지를 Model에 저장
		model.addAttribute("errorType", e.getType());
		model.addAttribute("errorMsg", e.getMessage());
		return "views/member/error";
	}
	
}
