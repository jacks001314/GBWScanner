package com.gbw.scanner;

import com.gbw.scanner.cmd.GBWCmdConstants;
import com.gbw.scanner.cmd.GBWCmdHandle;
import com.gbw.scanner.cmd.GBWCmdMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWScannerCmdHandle implements GBWCmdHandle {

    private static final Logger log = LoggerFactory.getLogger(GBWScannerCmdHandle.class);

    @Override
    public boolean isAccept(GBWCmdMessage message) {
        return message.getName().equals(GBWCmdConstants.exit);
    }

    @Override
    public void handle(GBWCmdMessage message) {

        log.info("Exit GBWScanner------------------------------------");
        System.exit(-1);
    }

}
