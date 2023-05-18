package com.Ronenii.Kaplat_server_exercise;

import com.Ronenii.Kaplat_server_exercise.Controller.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(App.class)
public class KaplatServerExerciseApplication {

	public static void main(String[] args) {
		String currentWorkingDir = System.getProperty("user.dir");
		System.out.println("Current Working Directory: " + currentWorkingDir);
		SpringApplication.run(KaplatServerExerciseApplication.class, args);
	}

}
