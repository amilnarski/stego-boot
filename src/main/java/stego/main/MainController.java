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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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
        int[] msgPixelArray = new int[messageImg.getWidth() * messageImg.getHeight()];
        messageImg.getRGB(0, 0, messageImg.getWidth(), messageImg.getHeight(), msgPixelArray, 0, messageImg.getWidth());


        BufferedImage coverImg = Sanselan.getBufferedImage(cover.getInputStream());
        int[] coverPixelArray = new int[messageImg.getWidth() * messageImg.getHeight()];
        coverImg.getRGB(0, 0, messageImg.getWidth(), messageImg.getHeight(), coverPixelArray, 0, messageImg.getWidth());


        for(int index = 0; index < msgPixelArray.length; index++ ){
            final Color msgRGB = new Color(msgPixelArray[index]);
            final Color coverColor = new Color(coverPixelArray[index]);
            final Color msgColor = new Color(StegoTool.getBit(msgRGB.getRed(), 8), StegoTool.getBit(msgRGB.getGreen(), 8), StegoTool.getBit(msgRGB.getBlue(), 8));
            final Color embedColor = new Color(replaceLSB(coverColor.getRed(), msgColor.getRed()), replaceLSB(coverColor.getGreen(), msgColor.getGreen()), replaceLSB(coverColor.getBlue(), msgColor.getBlue()));
            coverPixelArray[index] = embedColor.getRGB();
        }

        coverImg.setRGB(0, 0, messageImg.getWidth(), messageImg.getHeight(), coverPixelArray, 0, messageImg.getWidth());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(coverImg, "png", outputStream);
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        imageOutput.addObject("name", name);
        return imageOutput;
    }

    @RequestMapping(value = "/decode", method = RequestMethod.POST)
    public ModelAndView decode(@RequestParam("stego-object") MultipartFile stegoObject) throws IOException, ImageReadException {
        final ModelAndView imageOutput = new ModelAndView("imageOutput");

        final BufferedImage image = Sanselan.getBufferedImage(stegoObject.getInputStream());
        int[] msgPixelArray = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), msgPixelArray, 0, image.getWidth());

        for(int index = 0; index < msgPixelArray.length; index++ ) {
            final Color color = new Color(msgPixelArray[index]);
            final Color lsbColor = new Color(255 * getLSB(color.getRed()), 255 * getLSB(color.getGreen()), 255 * getLSB(color.getBlue()));
            msgPixelArray[index] = lsbColor.getRGB();
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), msgPixelArray, 0, image.getWidth());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        imageOutput.addObject("image64", Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        return imageOutput;
    }

    @RequestMapping(value = "/output", method = RequestMethod.GET)
    public ModelAndView imageOutput(){
        return new ModelAndView("imageOutput");
    }
}