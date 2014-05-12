package com.alibaba.jianchi.android.snapshot.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.alibaba.jianchi.android.snapshot.adb.AdbSnapShot;
import com.alibaba.jianchi.android.snapshot.util.ImageUtil;
import com.android.ddmlib.RawImage;

/**
 * TODO Comment of MainWindow
 * 
 * @author tanfeng
 */
public class MainWindow {

    private JFrame               mainFrame;
    private JButton              startButton;
    //刷新帧数设置
    private JTextField framesEdit ;
    private int frames=40;
    private PixelBufferComponent pixelBufferComponent;
    private AdbSnapShot          adbSnapShot;
    //是否正在捕获中
    private volatile boolean isCapturing = false;
    private boolean landscape = false;
    private JPanel contentPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private boolean isCaptureInited = false;
    JRadioButton horizontal,vertical;
    //自适应窗口大小
    private boolean adaptWindowVisible = true;

    public MainWindow() {
        init();
    }

    public void init() {
        mainFrame = new JFrame("android Capture");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setSize(screenSize);
        mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                doClose(e);
            }
        });
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        startButton = new JButton("开始");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startButton.setEnabled(false);
                    if(!isCapturing){
                    if(framesEdit.getText()!=null){
                    frames = Integer.valueOf(framesEdit.getText());}
                    doStartCapture();
                    if(isCapturing){
                        startButton.setText("停止");
                        startButton.setEnabled(true);
                    }
                    }else{
                        isCapturing = false;
                        startButton.setText("开始");
                    }
                }finally{
                    startButton.setEnabled(true);
                }

            }

        });
        mainFrame.getContentPane().add(contentPanel);
        contentPanel.setLayout(new BorderLayout());
        
        contentPanel.add(controlPanel, BorderLayout.LINE_START);
        int controlPanelwidth = 150;
        controlPanel.setPreferredSize(new Dimension(controlPanelwidth,400));
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
        controlPanel.add(startButton);
        startButton.setSize(new Dimension(controlPanelwidth,30));
        framesEdit = new JTextField();
        framesEdit.setDocument(new NumOnlyDocument());
        framesEdit.setColumns(5);
        framesEdit.setText(String.valueOf(frames));
        controlPanel.add(createRowPanel(new JLabel("刷新频率:"),framesEdit));
        
        horizontal = new JRadioButton("横屏");
        horizontal.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
               doScreenModelChange(e);
            }
            
        });
        
        vertical =new JRadioButton("竖屏");
        vertical.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
               doScreenModelChange(e);
            }
            
        });
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(horizontal);
        radioGroup.add(vertical);
        vertical.setSelected(true);
        controlPanel.add(createRowPanel(horizontal,vertical));
        mainFrame.setVisible(true);
        adbSnapShot = new AdbSnapShot();
    }
    
    /**
     * 
     */
    protected void doScreenModelChange(ChangeEvent e) {
        if(e.getSource()==vertical){
            landscape = false;
        }else if(e.getSource()==horizontal){
            landscape= true;
        }
        
    }

    private JPanel createRowPanel(JComponent c1,JComponent c2){
        JPanel panel = new JPanel();
        panel.add(c1);
        panel.add(c2);
        return panel;
    }

    private void doClose(WindowEvent e) {
        isCapturing=false;
        adbSnapShot.stop();
    }

    private void doStartCapture() {
        if(initCapture()){
            
            Thread captureThread = new Thread(new Runnable(){

                @Override
                public void run() {
                    
                    doCapture();
                }
                
            },"capture-thread");
            captureThread.start();
            isCapturing=true;
        }
    }
    
    /**
     * 
     */
    protected void doCapture() {
       int sleeptime =(int) 1000.0/frames;
       while(isCapturing){
           RawImage rawScreen= adbSnapShot.getScreenShot(landscape);
           if(rawScreen!=null){
               fillImageBuffer(rawScreen);
           }
           try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       }
        
    }

    private void fillImageBuffer(RawImage rawScreen){
        BufferedImage image=  new BufferedImage(rawScreen.width,rawScreen.height,BufferedImage.TYPE_INT_RGB);
        int index = 0;
        int indexInc = rawScreen.bpp >> 3;
        for (int y = 0; y < rawScreen.height; y++) {
               for (int x = 0; x < rawScreen.width; x++, index += indexInc) {
                      int value = rawScreen.getARGB(index);
                      image.setRGB(x, y, value);
               }
        }
        BufferedImage cache=pixelBufferComponent.getCacheImageBuffer();
        if(cache.getHeight()!=rawScreen.height||cache.getWidth()!=rawScreen.width){
            ImageUtil.resize(image,cache.getWidth(),cache.getHeight(), cache);
        }else{
            pixelBufferComponent.getCacheImageBuffer().setData(image.getData());
        }
        pixelBufferComponent.switchBuffer(); 
    }
    private boolean initCapture(){
        adbSnapShot.init();
        RawImage rawImage= adbSnapShot.getScreenShot(landscape);
        if(rawImage==null){
            JOptionPane.showConfirmDialog(mainFrame, "没有找到设备","错误",JOptionPane.OK_CANCEL_OPTION);
            isCapturing = false;
            return false;
        }
        if(pixelBufferComponent!=null){
           contentPanel.remove(pixelBufferComponent);
        }
        Dimension size = getImageSize(rawImage);
        pixelBufferComponent = new PixelBufferComponent(size.width,size.height);
        pixelBufferComponent.setVisible(true);
        contentPanel.add(pixelBufferComponent, BorderLayout.CENTER);
        contentPanel.validate();
        contentPanel.repaint();
        fillImageBuffer(rawImage);
        return true;
    }

    
    private Dimension getImageSize(RawImage rawImage){
          Dimension d= new Dimension();
          int width = mainFrame.getWidth();
          int height = mainFrame.getHeight();
          if(adaptWindowVisible&&(width<rawImage.width||height<rawImage.height)){
              double sx = width/(double)rawImage.width;
              double sy = height/(double)rawImage.height;
              if(sx<sy){
                  height = (int) (rawImage.height*sx);
              }else{
                  width = (int) (rawImage.width*sy);
              }
              d.setSize(width,height);
          }else{
              d.setSize(rawImage.width , rawImage.height);
          }
          return d;
    }
    
    
    public static void main(String args[]){
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System
                .getProperty("com.android.screenshot.bindir"));
       MainWindow mainWindwo = new MainWindow();
        
    }
    
    class NumOnlyDocument extends PlainDocument {
        public void insertString(int offset, String s, AttributeSet attrSet)
                throws BadLocationException {
            try {
                Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                return;
            }
            super.insertString(offset, s, attrSet);
        }
    }
}
