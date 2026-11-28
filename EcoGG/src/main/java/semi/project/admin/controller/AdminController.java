package semi.project.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import semi.project.member.service.MemberService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final MemberService mService;
	
	@GetMapping("/home")
	public String moveToMainAdmin(Model model) {
		int memberCount = mService.selectMemberCount();
		model.addAttribute("memberCount", memberCount);
		return "views/admin/admin";
	}

}
