package semi.project.member.controller;

import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
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

	@GetMapping("/login")
	public String loginView() {
		return "/views/member/login";
	}
	
	@PostMapping("/login")
	public String login(@ModelAttribute Member member, HttpSession session) {
		Member loginUser = mService.login(member);
		
		if(loginUser != null && bcrypt.matches(member.getMemberPwd(), loginUser.getMemberPwd())) {
			if(!loginUser.getMemberStatus().equals("SUSPENDED")) {
				session.setAttribute("loginUser", loginUser);
				
				if(loginUser.getSeparateMem().equals("Y")) {
					return "redirect:/admin/home";
				} else
					return "redirect:/home";
			} else {
				return "views/member/login";
			}
		} else {
			throw new MemberException("로그인을 실패했습니다.");
		}
	}
	
	@GetMapping("/enroll")
	public String enrollView() {
		return "views/member/enroll";
	}
	
	@PostMapping("/enroll")
	public String insertMember (@ModelAttribute Member member, @RequestParam("emailId") String emailId, @RequestParam("emailDomain") String emailDomain, HttpSession session) {
		if(member.getMemberId() == null || member.getMemberId().isBlank() || member.getMemberId().trim().length() < 2) return "redirect:/member/enroll";
		if(member.getMemberPwd() == null || member.getMemberPwd().isBlank()) return "redirect:/member/enroll";
		if(!member.getMemberPwd().matches(".*[a-zA-Z].*") || !member.getMemberPwd().matches(".*[0-9].*") || !member.getMemberPwd().matches(".*[!?@#$%&*].*") || !member.getMemberPwd().matches("^[a-zA-Z0-9!?@#$%&*]{8,16}$")) return "redirect:/member/enroll";
		if(member.getMemberName() == null || member.getMemberName().isBlank()) return "redirect:/member/enroll";
		if(member.getMemberNickName() == null || member.getMemberNickName().isBlank() || member.getMemberNickName().trim().length() < 2) return "redirect:/member/enroll";
		if(emailId == null || emailId.trim().isBlank()) return "redirect:/member/enroll";
		if(!emailDomain.equals("naver.com") && !emailDomain.equals("gmail.com") && !emailDomain.equals("hanmail.net") && !emailDomain.equals("nate.com")) return "redirect:/member/enroll";
		
		member.setMemberEmail(emailId + "@" + emailDomain);
		String verifyEmail = (String)session.getAttribute("verifyEmail");
		if(verifyEmail == null || !member.getMemberEmail().equals(verifyEmail)) return "redirect:/member/enroll";
		

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
		
		member.setMemberPwd(bcrypt.encode(member.getMemberPwd()));
		
		int result = mService.insertMember(member);
		if(result > 0) {
			session.removeAttribute("email");
			session.removeAttribute("random");
			session.removeAttribute("verifyEmail");
			session.removeAttribute("emailSentTime");
			return "redirect:/member/login";
		} else {
			throw new MemberException("회원가입에 실패했습니다.");
		}
	}
	
	@GetMapping("/mypage")
	public String mypage() {
		return "views/member/mypage";
	}
}
