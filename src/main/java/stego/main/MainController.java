package stego.main;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import stego.StegoTool;
import stego.imaging.PixelKey;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static stego.StegoTool.getLSB;
import static stego.StegoTool.replaceLSB;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String stego(){
        return "stego-boot";
    }

    @RequestMapping(value = "/encode", method = RequestMethod.POST)
    public ModelAndView encode(@RequestParam("cover") MultipartFile cover, @RequestParam("message") MultipartFile message, @RequestParam("name") String name) throws IOException, ImageReadException {
        final ModelAndView imageOutput = new ModelAndView("imageOutput");

        BufferedImage messageImg = Sanselan.getBufferedImage(message.getInputStream());
        final Map<PixelKey, Color> messagePixels = new ConcurrentHashMap<>(messageImg.getWidth()*messageImg.getHeight());
        for (int x = 0; x < messageImg.getWidth(); x++) {
            for (int y = 0; y < messageImg.getHeight(); y++) {
                Color messageColor = new Color(messageImg.getRGB(x, y));
                messagePixels.put(new PixelKey(x, y), new Color(StegoTool.getMSB(messageColor.getRed(), 8), StegoTool.getMSB(messageColor.getGreen(), 8), StegoTool.getMSB(messageColor.getBlue(), 8)));
            }
        }

        BufferedImage coverImg = Sanselan.getBufferedImage(cover.getInputStream());
        final Map<PixelKey, Color> coverPixels = new ConcurrentHashMap<>(coverImg.getWidth()*coverImg.getHeight());
        for (int x = 0; x < coverImg.getWidth(); x++) {
            for (int y = 0; y < coverImg.getHeight(); y++) {
                coverPixels.put(new PixelKey(x, y), new Color(coverImg.getRGB(x, y)));
            }
        }

        coverPixels.entrySet().parallelStream().filter(entry -> messagePixels.get(entry.getKey()) != null).forEach(e -> {
            PixelKey key = e.getKey();
            final Color msgColor = messagePixels.get(key);
            final Color coverColor = e.getValue();
            final Color embedColor = new Color(replaceLSB(coverColor.getRed(), msgColor.getRed()), replaceLSB(coverColor.getGreen(), msgColor.getGreen()), replaceLSB(coverColor.getBlue(), msgColor.getBlue()));
            coverImg.setRGB(key.getX(), key.getY(), embedColor.getRGB());
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(coverImg, "png", outputStream);
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        return imageOutput;
    }

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    public ModelAndView decode(@RequestParam("stego-object") MultipartFile stegoObject) throws IOException, ImageReadException {
        final ModelAndView imageOutput = new ModelAndView("imageOutput");

        final BufferedImage image = Sanselan.getBufferedImage(stegoObject.getInputStream());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final Color color = new Color(image.getRGB(x, y));
                final Color lsbColor = new Color(255*getLSB(color.getRed()), 255*getLSB(color.getGreen()), 255*getLSB(color.getBlue()));
                image.setRGB(x, y, lsbColor.getRGB());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        return imageOutput;
    }
}