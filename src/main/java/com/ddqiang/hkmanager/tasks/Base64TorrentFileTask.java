package com.ddqiang.hkmanager.tasks;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.Callable;

public class Base64TorrentFileTask implements Callable<String> {

    private File torrentFile;

    public Base64TorrentFileTask(File torrentFile) {
        this.torrentFile = torrentFile;
    }

    @Override
    public String call() throws Exception {
        if(torrentFile == null){
            return null;
        }
        byte [] data = Files.readAllBytes(Paths.get(torrentFile.getAbsolutePath()));

        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(data);
    }
}
