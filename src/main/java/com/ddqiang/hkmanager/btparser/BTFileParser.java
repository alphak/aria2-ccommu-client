package com.ddqiang.hkmanager.btparser;

import com.turn.ttorrent.common.TorrentFile;
import com.turn.ttorrent.common.TorrentMetadata;
import com.turn.ttorrent.common.TorrentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class BTFileParser {
    private final static Logger logger = LoggerFactory.getLogger(BTFileParser.class);

    private TorrentParser parser;

    @PostConstruct
    void init(){
        parser = new TorrentParser();
    }

    public List<TorrentFile> parseTorrentFile(File torrent) throws IOException {
        TorrentMetadata t = parser.parseFromFile(torrent);
        return t.getFiles();
    }
}
