package com.igt.demo.microservice.betting;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController("betting")
public class BettingController {

	@Autowired
	private BettingService service;

	@PostMapping("/capture")
	public long capture(Bet bet) {
		return service.capture(bet);
	}
}
