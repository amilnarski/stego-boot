package stego.main;

import org.apache.sanselan.Sanselan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import stego.StegoTool;
import stego.imaging.Pixel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String stego(){
        return "stego-boot";
    }

    @RequestMapping(value = "/encode", method = RequestMethod.POST)
    public ModelAndView encode(@RequestParam("cover") MultipartFile cover, @RequestParam("message") MultipartFile message, @RequestParam("name") String name) throws IOException {
        final ModelAndView imageOutput = new ModelAndView("imageOutput");
        try {
            BufferedImage messageImg = Sanselan.getBufferedImage(message.getInputStream());
            final List<Pixel> messagePixels = newArrayList();
            for (int x = 0; x < messageImg.getWidth(); x++) {
                for (int y = 0; y < messageImg.getHeight(); y++) {
                    messagePixels.add(new Pixel(x, y, messageImg.getRGB(x, y)));
                }
            }

            BufferedImage coverImg = Sanselan.getBufferedImage(message.getInputStream());
            final List<Pixel> coverPixels = newArrayList();
            for (int x = 0; x < coverImg.getWidth(); x++) {
                for (int y = 0; y < coverImg.getHeight(); y++) {
                    coverPixels.add(new Pixel(x, y, coverImg.getRGB(x, y)));
                }
            }

            final List<Pixel> messageMsbs = messagePixels.stream()
                    .map(p -> new Pixel(p.getX(), p.getY(), StegoTool.getMSB(p.getRed(), 8), StegoTool.getMSB(p.getGreen(), 8), StegoTool.getMSB(p.getBlue(), 8)))
                    .collect(toList());

            final List<Pixel> coverLsbs = coverPixels.stream()
                    .map(p -> new Pixel(p.getX(), p.getY(), StegoTool.getLSB(p.getRed()), StegoTool.getLSB(p.getGreen()), StegoTool.getLSB(p.getBlue())))
                    .collect(toList());

            imageOutput.addObject("image64", Base64.getEncoder().encodeToString(cover.getBytes()));
            return imageOutput;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
