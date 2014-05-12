package com.alibaba.jianchi.android.snapshot.adb;

import java.io.File;

/**
 * TODO Comment of AdbServer
 * @author tanfeng
 *
 */
public class AdbServer {
    
    private static AdbServer server;
    protected String adbPath;
    private Launcher launcher;
    
    
    private AdbServer(String adbPath,Launcher launcher){
        this.adbPath = adbPath;
        this.launcher = launcher;
    }
    
    public static synchronized AdbServer getServer(){
        if(server!=null){
            return server;
        }
        String adbPath = getAdbPath();
        Launcher launcher = LauntcherFactory.getLauntcher();
        launcher.setAdbPath(adbPath);
        server =new AdbServer(adbPath,launcher);
        return server;
    }
    
    
    
    /**
     * @return
     */
    private static String getAdbPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String arche = System.getProperty("os.arch").toLowerCase();
        String baseDir = System.getProperty("user.dir");
        baseDir=baseDir+File.separator+"adb";
        if("windows".equals(osName)){
            return baseDir+File.separator+"windows"+File.separator+"adb.exe";
        }
        if("linux".equals(osName)){
            if("amd64".equals(arche)){
                return baseDir+File.separator+"linux"+File.separator+"amd64"+File.separator+"adb";
            }else{
                return baseDir+File.separator+"linux"+File.separator+"x86"+File.separator+"adb";
            }
        }
        return null;
    }
    
    

    public boolean startServer(){
        return launcher.startServer();
    }
    
    public  boolean isServerRunning(){
        return launcher.isRunning();
    }
    
    public boolean stopServer(){
        return launcher.stopServer();
    }
    

}
