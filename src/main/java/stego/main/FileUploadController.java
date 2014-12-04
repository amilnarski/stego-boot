package stego.main;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageReader;

@Controller
public class FileUploadController {

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public String provideUploadInfo() {
        return "fileUpload";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file){
        ModelAndView modelAndView = new ModelAndView("imageOutput");
        String name = file.getName();
            try {
                byte[] bytes = file.getBytes();
//                BufferedImage img = new BufferedImage()
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                modelAndView.addObject("image64", Base64.getEncoder().encodeToString(bytes));
                modelAndView.addObject("name", name);
                return modelAndView;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

}
