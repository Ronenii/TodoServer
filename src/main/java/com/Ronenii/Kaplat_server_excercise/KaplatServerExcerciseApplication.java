package com.Ronenii.Kaplat_server_excercise;

import com.Ronenii.Kaplat_server_excercise.Controller.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(App.class)
public class KaplatServerExcerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaplatServerExcerciseApplication.class, args);
	}

}
