package com.gbw.scanner.plugins.bruteforce.mssql;

import com.gbw.scanner.Host;
import com.gbw.scanner.plugins.bruteforce.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GBWBruteForceMSSQL extends GBWBruteForceJDBC {

    private GBWBruteForceMSSQLConfig mssqlConfig;

    public GBWBruteForceMSSQL(GBWBruteForceMSSQLConfig config) throws IOException, InterruptedException {
        super(config);
    }

    @Override
    public String makeURL(Host host,GBWDictEntry entry){

        return String.format("jdbc:sqlserver://%s:%d;databaseName=;user=%s;password=%s;",host.getServer(),
                host.getPort(),entry.getUser(),entry.getPasswd());
    }

    @Override
    public Connection makeConnection(Host host, GBWDictEntry entry) throws SQLException {

        String url = makeURL(host,entry);

        return DriverManager.getConnection(url);
    }

    @Override
    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry) {

        return super.bruteForce(host,entry,GBWBruteForcePlugin.BRUTEFORCEMSSQL);
    }


}
