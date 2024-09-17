package br.com.langoni.screenmatchSpring;

import br.com.langoni.screenmatchSpring.model.SeriesData;
import br.com.langoni.screenmatchSpring.service.ChangeData;
import br.com.langoni.screenmatchSpring.service.ConsumeApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumeApi = new ConsumeApi();
		var json = consumeApi.getData("http://www.omdbapi.com/?apikey=eb8d937f&t=simpsons");
		System.out.println(json);
		ChangeData converter = new ChangeData();
		SeriesData seriesData = converter.getData(json, SeriesData.class);
		System.out.println(seriesData);
	}
}
