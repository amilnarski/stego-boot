package stego.main;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import stego.StegoTool;
import stego.imaging.Pixel;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static stego.StegoTool.getLSB;

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
        final List<Pixel> messagePixels = newArrayList();
        for (int x = 0; x < messageImg.getWidth(); x++) {
            for (int y = 0; y < messageImg.getHeight(); y++) {
                messagePixels.add(new Pixel(x, y, messageImg.getRGB(x, y)));
            }
        }

        BufferedImage coverImg = Sanselan.getBufferedImage(cover.getInputStream());
        final Map<Pixel.PixelKey, Pixel> coverPixels = newHashMap();
        for (int x = 0; x < coverImg.getWidth(); x++) {
            for (int y = 0; y < coverImg.getHeight(); y++) {
                final Pixel pixel = new Pixel(x, y, coverImg.getRGB(x, y));
                coverPixels.put(pixel.getKey(), pixel);
            }
        }

        final Map<Pixel.PixelKey, Pixel> pixelMap = messagePixels.parallelStream()
                .map(p -> new Pixel(p.getX(), p.getY(), StegoTool.getMSB(p.getRed(), 8), StegoTool.getMSB(p.getGreen(), 8), StegoTool.getMSB(p.getBlue(), 8)))
                .collect(Collectors.toMap(Pixel::getKey, p -> p));

        for (int x = 0; x < coverImg.getWidth(); x++) {
            for (int y = 0; y < coverImg.getHeight(); y++) {
                final Pixel.PixelKey key = new Pixel.PixelKey(x, y);
                final Pixel messageMSBPixel = pixelMap.get(key);
                if (messageMSBPixel != null) {
                    final Pixel coverPixel = coverPixels.get(key);

                    final int redEmbed = coverPixel.getRed() | messageMSBPixel.getRed();
                    final int greenEmbed = coverPixel.getGreen() | messageMSBPixel.getGreen();
                    final int blueEmbed = coverPixel.getBlue() | messageMSBPixel.getBlue();

                    coverImg.setRGB(x, y, new Pixel(coverPixel.getX(), coverPixel.getY(), redEmbed, greenEmbed, blueEmbed).getRGB());
                }
            }
        }

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
                final Color lsbColor = new Color(getLSB(color.getRed()), getLSB(color.getGreen()), getLSB(color.getBlue()));
                image.setRGB(x, y, lsbColor.getRGB());

            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        return imageOutput;
    }
}
