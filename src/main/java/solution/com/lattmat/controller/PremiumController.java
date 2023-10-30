package solution.com.lattmat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/premium")
public class PremiumController {

    @PostMapping("/home")
    public ResponseEntity<String> convert(){
        return ResponseEntity.ok("Welcome premium user...");
    }
}
