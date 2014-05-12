package com.alibaba.jianchi.android.snapshot.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * TODO Comment of ImageUtil
 * @author tanfeng
 *
 */
public class ImageUtil {
        /**  
         * 实现图像的等比缩放  
         * @param source  
         * @param targetW  
         * @param targetH  
         * @return  
         */  
        public static void resize(BufferedImage source, int targetW,   
                int targetH,BufferedImage target) {   
            Image scaleImage=  source.getScaledInstance(targetW, targetH, Image.SCALE_FAST);
            target.getGraphics().drawImage(scaleImage, 0, 0, targetW, targetH, null);
        }   
}
