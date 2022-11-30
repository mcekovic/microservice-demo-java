package com.igt.demo.microservice.betting;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/betting")
public class BettingController {

	@Autowired
	private BettingService service;

	@PostMapping(path = "/capture", consumes = MediaType.APPLICATION_JSON_VALUE)
	public long capture(Bet bet) {
		return service.capture(bet);
	}

	@GetMapping(path = "/betCount")
	public int betCount() {
		return service.betCount();
	}
}
