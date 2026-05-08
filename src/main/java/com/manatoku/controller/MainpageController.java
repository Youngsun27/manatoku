package com.manatoku.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainpageController {
	
	@GetMapping("/home")
	public String home(HttpSession session) {
		session.removeAttribute("GAME_ID");
        session.removeAttribute("LAST_END_WORD");
		return "/mainpage/mainpage";
	}
}
