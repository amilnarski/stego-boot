package stego.main;

import java.awt.image.BufferedImage;
import java.util.Base64;
import java.util.List;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import stego.StegoTool;
import stego.imaging.Pixel;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

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
                final List<Pixel> pixels = newArrayList();
                for(int x = 0; x < img.getWidth(); x++){
                    for(int y = 0; y < img.getHeight(); y++) {
                        pixels.add(new Pixel(x, y, img.getRGB(x, y)));
                    }
                }

                final List<Pixel> pixelMsbs = pixels.stream()
                        .map(p -> new Pixel(p.getX(), p.getY(), StegoTool.getMSB(p.getRed(), 8), StegoTool.getMSB(p.getGreen(), 8), StegoTool.getMSB(p.getBlue(), 8)))
                        .collect(toList());

//                final List<Integer> blueLsbs = blueList.stream().map(StegoTool::getLSB).collect(toList());




                final ImageInfo imageInfo = Sanselan.getImageInfo(file.getBytes());
                final int bitsPerPixel = imageInfo.getBitsPerPixel();
                System.out.print("Bits/pixel — " + bitsPerPixel);
                modelAndView.addObject("image64", Base64.getEncoder().encodeToString(file.getBytes()));
                modelAndView.addObject("rgbArray", pixelMsbs);
                modelAndView.addObject("name", name);
                return modelAndView;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }

}
