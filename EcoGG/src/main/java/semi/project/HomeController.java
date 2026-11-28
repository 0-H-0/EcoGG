package semi.project;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class HomeController {
	
	@GetMapping("/home")
	public String goHome () {
		return "views/home";
	}
	
//	// Http 에러 강제발생 메소드
//	@GetMapping("/testError")
//	public void testError() {
//		throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
//	}
}
