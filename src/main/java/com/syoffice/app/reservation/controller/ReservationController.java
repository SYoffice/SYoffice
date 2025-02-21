package com.syoffice.app.reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/reservation/*")
public class ReservationController {
	
	@GetMapping("meetingRoomReservation")
	public String meetingRoomReservation() {
		
		
		return "/reservation/meetingRoomReservation";
	}
	
	@GetMapping("rentcarReservation")
	public String rentcarReservation() {
		
		
		return "/reservation/rentcarReservation";
	}
}
