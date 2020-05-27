package com.gbw.scanner.source;

import com.gbw.scanner.cmd.GBWCmdConstants;
import com.gbw.scanner.cmd.GBWCmdHandle;
import com.gbw.scanner.cmd.GBWCmdMessage;
import com.gbw.scanner.utils.Base64Utils;
import com.gbw.scanner.utils.GsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GBWHostSourceCmdHandle implements GBWCmdHandle {

    private static final Logger log = LoggerFactory.getLogger(GBWHostSourceCmdHandle.class);
    private GBWHostSourcePool sourcePool;

    public GBWHostSourceCmdHandle(GBWHostSourcePool sourcePool){

        this.sourcePool = sourcePool;
    }


    @Override
    public boolean isAccept(GBWCmdMessage message) {
        return message.getName().equals(GBWCmdConstants.addSourceCmd);
    }

    @Override
    public void handle(GBWCmdMessage message) {

        String content = Base64Utils.decode(message.getContent());
        GBWHostSourceCmdMesage cmdMesage = null;

        try {

            cmdMesage = GsonUtils.loadConfigFromJson(content,GBWHostSourceCmdMesage.class);

            GBWHostSource hostSource = GBWHostSourceFactory.create(cmdMesage.getType(),Base64Utils.decode(cmdMesage.getContent()));

            if(hostSource!=null){

                sourcePool.addSource(hostSource);
                log.info(String.format("Add a Host Source:%s ok!",cmdMesage.getType()));
            }else{

                log.info(String.format("Add a Host Source Failed,unkown source:%s!",cmdMesage.getType()));
            }

        } catch (Exception e) {
            log.error(String.format("Add a Host Source:%s Failed,error:%s!",cmdMesage.getType(),e.getMessage()));
        }

    }


}
