package com.alibaba.jianchi.android.snapshot.adb;

/**
 * TODO Comment of LauntcherFactory
 * @author tanfeng
 *
 */
public class LauntcherFactory {
    
    public static Launcher getLauntcher(){
        String osName = System.getProperty("os.name").toLowerCase();
        
        if("windows".equals(osName)){
            return new WindowsLauncher();
        }
        if(("linux").equals(osName)){
            return new LinuxLauncher();
        }
        throw new RuntimeException("unsupport os. osName="+osName);
    }

}
