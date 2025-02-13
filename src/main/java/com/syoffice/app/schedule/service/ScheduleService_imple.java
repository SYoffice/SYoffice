package com.syoffice.app.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syoffice.app.schedule.model.ScheduleDAO;

@Service
public class ScheduleService_imple implements ScheduleService {
	
	@Autowired
	private ScheduleDAO dao;
	
}
