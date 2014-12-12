package stego.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String stego(){
        return "stego-boot";
    }

    @RequestMapping(value = "/encode", method = RequestMethod.POST)
    public ModelAndView encode(@RequestParam("cover") MultipartFile cover, @RequestParam("message") MultipartFile message, @RequestParam("name") String name) throws IOException {
        final ModelAndView imageOutput = new ModelAndView("imageOutput");
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(cover.getBytes()));
        return imageOutput;
    }
}
