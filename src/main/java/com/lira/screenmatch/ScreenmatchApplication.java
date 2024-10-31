package com.lira.screenmatch;

import com.lira.screenmatch.model.SeriesData;
import com.lira.screenmatch.service.ConsumeAPI;
import com.lira.screenmatch.service.ConvertData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumeApi = new ConsumeAPI();
		var json = consumeApi.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=ff3c9083");
		System.out.println(json);
		ConvertData converter = new ConvertData();
		SeriesData data = converter.getData(json, SeriesData.class);
		System.out.println(data);
	}
}
