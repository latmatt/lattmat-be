package solution.com.lattmat.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solution.com.lattmat.config.AppConfig;

@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController {

//    private final AppConfig appConfig;

    @Value("${redirectUrl:Hello default}")
    private String redirectUrl;


    @GetMapping
    public String hello(){

//        System.out.println(appConfig.getRedirectUrl());
        System.out.println(redirectUrl);

        return "HELLO";
    }

}
