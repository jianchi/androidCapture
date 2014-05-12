package com.alibaba.jianchi.android.snapshot.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * TODO Comment of PixelBufferComponent
 * @author tanfeng
 *
 */
public class PixelBufferComponent extends JComponent{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage imageBuffer1 ;
    private BufferedImage imageBuffer2;
    private volatile boolean swithCache = false;
    
    public PixelBufferComponent(int width,int height){
        imageBuffer1 = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        imageBuffer2 = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(swithCache){
            g.drawImage(imageBuffer1, 0, 0, null);
        }else{
            g.drawImage(imageBuffer2, 0, 0, null);
        }
    }
    
    public void switchBuffer(){
        swithCache = !swithCache;
        repaint();
    }
    
    public BufferedImage getCacheImageBuffer(){
        if(swithCache){
            return imageBuffer2;
        }else{
            return imageBuffer1;
        }
    }
   
}
