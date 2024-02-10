package com.Ronenii.Kaplat_server_exercise;

import com.Ronenii.Kaplat_server_exercise.controller.ServerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ServerController.class)
public class KaplatServerExerciseApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(KaplatServerExerciseApplication.class, args);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
