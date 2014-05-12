package com.alibaba.jianchi.android.snapshot.adb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO Comment of Linux64Launcher
 * 
 * @author tanfeng
 */
public class LinuxLauncher extends Launcher {

    private Process currentProcess;

    @Override
    public boolean startServer() {
        try {
            Process p = Runtime.getRuntime().exec(
                    "gksudo "+adbPath + " start-server");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = in.readLine();
            boolean flag = false;
            while (line != null) {
                if (line.contains("daemon started successfully")) {
                    flag = true;
                    break;
                }
                line = in.readLine();
            }
            if (flag) {
                currentProcess = p;
                return true;
            }
            return false;
        } catch (IOException e) {

        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.alibaba.jianchi.android.snapshot.adb.Launcher#stopServer()
     */
    @Override
    public boolean stopServer() {
        if (currentProcess != null) {
            currentProcess.destroy();
            currentProcess = null;
            return true;
        }
        try {
            Process p = Runtime.getRuntime().exec(adbPath + " kill-server");
            p.waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isRunning() {
        if (currentProcess != null) {
            return true;
        }
        //TODO 
        //when the currentProcess is null, check the adb process.
        try {
            Process p = Runtime.getRuntime().exec("ps aux | grep adb");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = in.readLine();
            boolean flag = false;
            while (line != null) {
                if (line.contains("fork-server server")) {
                    flag = true;
                    break;
                }
                line = in.readLine();
            }
            return flag;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}
