package stego.imaging;

import org.apache.sanselan.Sanselan;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class ImageReaderCallable implements Callable<Map<PixelKey, Color>>{
    final InputStream imageInputStream;

    public ImageReaderCallable(InputStream imageInputStream) {
        checkNotNull(imageInputStream);
        this.imageInputStream = imageInputStream;
    }

    @Override
    public Map<PixelKey, Color> call() throws Exception {
        BufferedImage image = Sanselan.getBufferedImage(imageInputStream);
        final Map<PixelKey, Color> imagePixels = new ConcurrentHashMap<>(image.getWidth()*image.getHeight());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                imagePixels.put(new PixelKey(x, y), new Color(image.getRGB(x, y)));
            }
        }
        return imagePixels;
    }
}
