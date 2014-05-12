package com.alibaba.jianchi.android.snapshot.adb;

/**
 * TODO Comment of Launcher
 * @author tanfeng
 *
 */
public  abstract class Launcher {
    
    protected String adbPath;
    
    public void setAdbPath(String adbPath){
        this.adbPath = adbPath;
    }
    
    public abstract boolean startServer();
    
    public abstract boolean stopServer();
    
    
    public abstract boolean isRunning();

}
