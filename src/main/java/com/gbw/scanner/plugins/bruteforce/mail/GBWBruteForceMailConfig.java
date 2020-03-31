package com.gbw.scanner.plugins.bruteforce.mail;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceMailConfig extends GBWBruteForceCommonConfig {

    private String proto;

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public void addOpts(Options opts){

        super.addOpts(opts);

        opts.addOption("proto",true,"mail proto(smtp[s],pop[s],imap[s]");

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{
        super.initFromOpts(cmdLine);

        proto = "smtp";

        if(cmdLine.hasOption("proto"))
            proto = cmdLine.getOptionValue("proto");


    }
}
