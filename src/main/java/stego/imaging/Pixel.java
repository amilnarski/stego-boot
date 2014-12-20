package stego.imaging;

import java.awt.*;

public class Pixel {
    private final int x;
    private final int y;
    private final int red;
    private final int green;
    private final int blue;

    public Pixel(int x, int y, int rgb) {
        this.x = x;
        this.y = y;
        final Color color = new Color(rgb);
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Pixel(int x, int y, int red, int green, int blue) {
        this.x = x;
        this.y = y;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getRGB(){
        return new Color(red, green, blue).getRGB();
    }

    public PixelKey getKey(){
        return new PixelKey(x, y);
    }

    public static class PixelKey {
        final int x;
        final int y;

        public PixelKey(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PixelKey pixelKey = (PixelKey) o;

            return x == pixelKey.x && y == pixelKey.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
