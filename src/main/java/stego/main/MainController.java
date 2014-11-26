package stego.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @RequestMapping("/")
    public String stego(){
        return "template";
    }
}
