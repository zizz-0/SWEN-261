package com.needs.api.needsapi;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NeedsApiApplication {

	public static void main(String[] args) throws IOException{
		SpringApplication.run(NeedsApiApplication.class, args);
		/*
		FundingBasket fb = new FundingBasket(); 
		fb.loadJasonFile("data/needs.json");
		System.out.println(fb);

		Need need = new Need(6, "test", NeedType.EQUIPMENT, 1.00, 1);

		fb.addNeed(need);
		fb.deleteNeed(1);
		System.out.println(fb);
		 */
	}

}
