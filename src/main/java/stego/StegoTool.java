package stego;

import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class StegoTool {

    public static int getLSB(int i){
//        checkArgument(i > -1);
        return i & 1;
    }

    public static int getMSB(int i){
        //check to make sure i is positive to avoid two's complement
        //shift left 16 to erase any high order bits,
        //then shift left so msb is in 1s place
//        checkArgument(i > -1);
        return (i << 16) >>> 31;
    }

    public static int getBit(int i, int position){
//        checkArgument(i > -1);
        return (i << 32-position) >>> 31;
    }

    public static int replaceLSB(int n, int lsb){
        return (n & ~1) | lsb;
    }
}
