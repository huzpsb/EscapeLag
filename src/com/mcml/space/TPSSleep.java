package com.mcml.space;

public class TPSSleep
        implements Runnable {

    public void run() {
        if (VLagger.TPSSleepenable == true) {
            try {
                Thread.sleep(VLagger.TPSSleepPeriod);
            } catch (InterruptedException ex) {
            }
        }
    }
}