package com.fu.common.util;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by manlm on 9/29/2016.
 */
public class ImageUtil {

    private static final Logger LOG = Logger.getLogger(AESUtil.class);

    private static final int DEFAULT_WIDTH = 475;
    private static final int DEFAULT_HEIGHT = 250;

    public ImageUtil() {
        // Default Constructor
    }

    public static void generateThumbNail(String url, String text) {
        LOG.info("[generateThumbNail] Start: url = " + url);

        try {
            BufferedImage image = ImageIO.read(new URL(url));

            int height = image.getHeight() * DEFAULT_WIDTH / image.getWidth();

            if (height < 250) {
                image = resize(image, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            } else {
                image = resize(image, DEFAULT_WIDTH, height);
                image = cropImage(image, 0, (height - DEFAULT_HEIGHT) / 2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }

            image = drawTextOnImage(text, image, 0);

            LOG.info("[generateThumbNail] End");
            ImageIO.write(image, "png", new File("test.png"));

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] imageInByte = baos.toByteArray();
//            baos.close();

        } catch (IOException e) {
            LOG.error("[generateThumbNail] IOException: " + e);
        }
    }

    /**
     * Draw text on image
     *
     * @param text
     * @param image
     * @param space
     * @return
     */
    private static BufferedImage drawTextOnImage(String text, BufferedImage image, int space) {
        LOG.info(new StringBuilder("[drawTextOnImage] Start: text = ").append(text)
                .append(", space = ").append(space));

        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight() + space, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g2d.drawImage(image, 0, 0, null);

        g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        FontMetrics fm = g2d.getFontMetrics();

        int x = (bi.getWidth() / 2) - fm.stringWidth(text) / 2;
        int y = bi.getHeight() / 2 + fm.getHeight() / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, shiftWest(x, 2), shiftNorth(y, 2));
        g2d.drawString(text, shiftWest(x, 2), shiftSouth(y, 2));
        g2d.drawString(text, shiftEast(x, 2), shiftNorth(y, 2));
        g2d.drawString(text, shiftEast(x, 2), shiftSouth(y, 2));

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
        g2d.dispose();

        LOG.info("[drawTextOnImage] End");
        return bi;
    }

    private static int shiftNorth(int p, int distance) {
        return p - distance;
    }

    private static int shiftSouth(int p, int distance) {
        return p + distance;
    }

    private static int shiftEast(int p, int distance) {
        return p + distance;
    }

    private static int shiftWest(int p, int distance) {
        return p - distance;
    }

    /**
     * Resize image
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        LOG.info(new StringBuilder("[resize] Start: width = ").append(width)
                .append(", height = ").append(height));

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        LOG.info("[resize] End");
        return bi;
    }

    /**
     * Crop image
     *
     * @param image
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private static BufferedImage cropImage(BufferedImage image, int x, int y, int width, int height) {
        LOG.info(new StringBuilder("[cropImage] Start: x = ").append(x)
                .append(", y = ").append(y)
                .append(", width = ").append(width)
                .append(", height = ").append(height));
        LOG.info("[cropImage] End");
        return image.getSubimage(x, y, width, height);
    }

    public static void main(String[] args) throws Exception {
        generateThumbNail("http://www.acecookvietnam.vn/media/posts/product/71433743318.png", "Hello World!!!");
    }
}
