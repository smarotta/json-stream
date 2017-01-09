package com.avenuecode.talk.stream.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@RequestMapping("/alive")
    public Object greeting() {
		
		return new Object() {
			public String getTest() {
				return UUID.randomUUID().toString();
			}
		};
		
    }
}
