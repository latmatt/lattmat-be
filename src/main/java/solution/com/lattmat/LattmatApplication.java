package solution.com.lattmat;

import org.modelmapper.ModelMapper;
import org.modelmapper.record.RecordModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LattmatApplication {

	public static void main(String[] args) {
		SpringApplication.run(LattmatApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper().registerModule(new RecordModule());
	}
}
