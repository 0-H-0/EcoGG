package semi.project.fetch.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import semi.project.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class FetchController {
	private final MemberService mService;
	private final JavaMailSender mailSender;

	@GetMapping("idNickname")
	public int checkValue(@RequestParam HashMap<String, String> map) {
		int count = mService.checkValue(map);
		return count;
	}
	
	@PostMapping("email")
	public boolean sendEmail(@RequestParam("email") String email, HttpSession session) {
		session.removeAttribute("email");
		session.removeAttribute("random");
		session.removeAttribute("verifyEmail");
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		String subject = "[EcoGG] 회원가입 이메일 확인";
		String body = "<h1 align='center'>EcoGG 이메일 확인</h1><br/>";
		body += "<div style='border: 3px solid skyblue; text-align: center; font-size: 15px'>";
		body += "본 메일은 사용자의 이메일을 확인하기 위해 발송되었습니다.<br/>";
		body += "아래 숫자를 인증번호 확인란에 작성하여 확인해주시기 바랍니다. <br/><br/>";
		
		String random = "";
		for(int i = 0; i < 5; i++) {
			random += (int)(Math.random()*10);
		}
		
		body += "<span style='font-size: 30px; text-decoration: underline;'><b>" + random;
		body += "</b></span><br/></div>";
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setFrom("ecogg.noreply@gmail.com");
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);
		session.setAttribute("email", email);
		session.setAttribute("random", random);
		long emailSentTime = System.currentTimeMillis();
		session.setAttribute("emailSentTime", emailSentTime);
		return true;
	}
	
	@PostMapping("verifyEmail")
	public int checkVerifyNum (@RequestParam("verifyNum") String verifyNum, HttpSession session) {
		String random = (String)session.getAttribute("random");
		long currentTime = System.currentTimeMillis();
		Long emailSentTime = (Long)session.getAttribute("emailSentTime");
		if(emailSentTime == null || random == null) {
			return -2;
		}
		if((currentTime - emailSentTime) >= 180000) {
			session.removeAttribute("random");
			session.removeAttribute("emailSentTime");
			return -1;
		}
		if(verifyNum.equals(random)) {
			session.setAttribute("verifyEmail", (String)session.getAttribute("email"));
			session.removeAttribute("random");
			return 1;
		} else {
			return 0;
		}
	}
}
