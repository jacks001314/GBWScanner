package com.gbw.scanner.plugins.bruteforce.mysql;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GBWBruteForceMySQL extends GBWBruteForceJDBC {

    private GBWBruteForceMySQLConfig mySQLConfig;

    public GBWBruteForceMySQL(GBWBruteForceMySQLConfig config) throws IOException, InterruptedException {
        super(config);
        this.mySQLConfig = config;
    }


    @Override
    public String makeURL(Host host,GBWDictEntry entry) {

        return String.format("jdbc:mysql://%s:%d/mysql?user=%s&password=%s&useSSL=%s",host.getServer(),host.getPort(),entry.getUser(),entry.getPasswd(),
                mySQLConfig.isSSL()?"true":"false");
    }

    @Override
    public Connection makeConnection(Host host, GBWDictEntry entry) throws SQLException {

        String url = String.format("jdbc:mysql://%s:%d/?useSSL=%s",host.getServer(),host.getPort(),mySQLConfig.isSSL()?"true":"false");
        return DriverManager.getConnection(url,entry.getUser(),entry.getPasswd());
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        return super.bruteForce(host,entry,GBWBruteForcePlugin.BRUTEFORCEMYSQL);
    }


}
