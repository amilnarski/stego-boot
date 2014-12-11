package stego;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class StegoTool {
    public BufferedImage embed(BufferedImage cover, BufferedImage message){
        checkArgument(cover.getHeight()*cover.getWidth()>= (message.getWidth()*message.getHeight())+32, "Cover is not large enough!");
        checkArgument(message.getHeight() <= 65536 && message.getWidth() <= 65536 , "Message may only have a maximum dimension of 65,536 pixels!");
        //build bits to embed
        StringBuilder toEmbed = new StringBuilder();
        String widthString = Integer.toBinaryString(message.getWidth());

//        if(widthString.)


        for(int x = 0; x < message.getHeight(); x++){
            for (int y = 0; y < message.getWidth(); y++){

            }
        }
        return null;
    }

    public static int getLSB(int i){
        return i << -1 >>> -1;
    }

    public static int getMSB(int i){
        return i >>> -2;
    }
}
