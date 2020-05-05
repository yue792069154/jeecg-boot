package org.jeecg.modules.home;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class eee {

    @GetMapping("/testee")
    public  String tt(){
        return  "1231";
    }
}
