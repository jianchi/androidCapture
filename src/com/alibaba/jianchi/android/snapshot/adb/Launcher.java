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
