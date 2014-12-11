package stego.main;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Base64;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
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
        String name = file.getOriginalFilename();
            try {
                BufferedImage img = Sanselan.getBufferedImage(file.getInputStream());
                int[][] rgb = new int[img.getWidth()][img.getHeight()];

                for(int x = 0; x < img.getWidth(); x++){
                    for(int y = 0; y < img.getHeight(); y++) {
                        rgb[x][y] = img.getRGB(x, y);
                    }
                }

                final ImageInfo imageInfo = Sanselan.getImageInfo(file.getBytes());
                final int bitsPerPixel = imageInfo.getBitsPerPixel();
                System.out.print("Bits/pixel — " + bitsPerPixel);
                modelAndView.addObject("image64", Base64.getEncoder().encodeToString(file.getBytes()));
                modelAndView.addObject("rgbArray", Arrays.toString(rgb));
                modelAndView.addObject("name", name);
                return modelAndView;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

}
