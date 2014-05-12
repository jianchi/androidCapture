/**
 * Project: AdbSnapshot
 * 
 * File Created at 2014-5-10
 * $Id$
 * 
 * Copyright 2008 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
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
