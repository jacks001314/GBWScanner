package com.gbw.scanner.plugins.bruteforce.ssh;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.List;

public class GBWBruteForceSSHConfig extends GBWBruteForceCommonConfig {

    private String cmd;
    private boolean ignoreEmpty;
    private List<String> passKeys;

    public void addOpts(Options opts){

        super.addOpts(opts);
        opts.addOption("cmd",true,"ssh run cmd!");
        opts.addOption("ignoreEmpty",false,"ignore empty!");
        opts.addOption("passKeys",true,"pass ssh result that contains this keys:<k1>,<k2>,.....");

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{

        super.initFromOpts(cmdLine);
        ignoreEmpty = false;

        cmd = "";
        this.passKeys = new ArrayList<>();

        if(cmdLine.hasOption("cmd"))
            cmd = cmdLine.getOptionValue("cmd");

        if(cmdLine.hasOption("ignoreEmpty"))
            ignoreEmpty = true;

        if(cmdLine.hasOption("passKeys"))
        {
            for(String k:cmdLine.getOptionValue("passKeys").split(",")){

                passKeys.add(k);
            }
        }
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<String> getPassKeys() {
        return passKeys;
    }

    public void setPassKeys(List<String> passKeys) {
        this.passKeys = passKeys;
    }

    public boolean isIgnoreEmpty() {
        return ignoreEmpty;
    }

    public void setIgnoreEmpty(boolean ignoreEmpty) {
        this.ignoreEmpty = ignoreEmpty;
    }
}
