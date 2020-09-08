package com.menazord.videograb.forms;

import com.menazord.videograb.model.DownloadStatus;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ProgressTask extends SwingWorker<Void, Void> {

    private DownloadStatus status;

    public ProgressTask(DownloadStatus status) {
        this.status = status;
    }

    /*
         * Main task. Executed in background thread.
         */
    @Override
    public Void doInBackground() {

        Random random = new Random();
        int progress = 0;
        //Initialize progress property.
        setProgress(0);
        while (progress < 100) {
            //Sleep for up to one second.
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException ignore) {}

            progress = status.getProgress();
            setProgress(Math.min(progress, 100));
        }
        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        Toolkit.getDefaultToolkit().beep();
    }
}
