package br.com.langoni.screenmatchSpring;

import br.com.langoni.screenmatchSpring.main.Main;
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
        Main main = new Main();
        main.showMenu();
    }
}