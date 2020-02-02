package com.gbw.scanner.plugins.bruteforce;

import com.gbw.scanner.Host;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class GBWBruteForceJDBC extends GBWAbstractBruteForce {


    public GBWBruteForceJDBC(GBWBruteForceCommonConfig config) throws IOException, InterruptedException {
        super(config);
    }

    public abstract String makeURL(Host host, GBWDictEntry entry);

    public abstract Connection makeConnection(Host host,GBWDictEntry entry) throws SQLException;

    public GBWBruteForceResult bruteForce(Host host, GBWDictEntry entry,String type) {


        Connection con = null;
        try {

            con = makeConnection(host,entry);
            if(con!=null){

                con.close();
                return new GBWBruteForceResult(entry,host,type);
            }

        } catch (SQLException e) {
            return null;
        }finally {

            if(con!=null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;

    }
}
