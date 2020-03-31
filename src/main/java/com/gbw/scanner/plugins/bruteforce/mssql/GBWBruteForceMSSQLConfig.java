package com.gbw.scanner.plugins.bruteforce.mssql;

import com.gbw.scanner.plugins.bruteforce.GBWBruteForceCommonConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class GBWBruteForceMSSQLConfig extends GBWBruteForceCommonConfig {

    public void addOpts(Options opts){

        super.addOpts(opts);

    }

    public void initFromOpts(CommandLine cmdLine) throws IllegalArgumentException{
        super.initFromOpts(cmdLine);



    }

}
