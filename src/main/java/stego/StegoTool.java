package stego;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
        checkArgument(i >= 0);
        return i & 1;
    }

    public static int getMSB(int i){
        //check to make sure i is positive to avoid two's complement
        //shift left 16 to erase any high order bits,
        //then shift left so msb is in 1s place
        if(i < 0){
            throw new IllegalArgumentException("NOPE");
        }
        return (i << 16) >>> 31;
    }

    public static int getMSB(int i, int position){
        checkArgument(i > -1);
        return (i << 32-position) >>> 31;
    }
}
