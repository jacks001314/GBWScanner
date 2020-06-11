package com.gbw.scanner.utils;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.Path;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilsMain {


    public static void main(String[] args) throws Exception {

        Options opts = new Options();

        opts.addOption("toHex",true,"string to hex");
        opts.addOption("toHexF",true,"file content to hex");
        opts.addOption("enBase64",true,"string to base64");
        opts.addOption("enBase64f",true,"to base64 from file");
        opts.addOption("deBase64",true,"decode base64 from string");
        opts.addOption("deBase64f",true,"decode base64 from file");

        opts.addOption("enURL",true,"encode url from string");
        opts.addOption("deURL",true,"decode url from string");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){

            new HelpFormatter().printHelp("UtilsMain", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("toHex")){

            System.out.println(ByteDataUtils.toHex(cliParser.getOptionValue("toHex")));
        }

        if(cliParser.hasOption("toHexF")){

            System.out.println(ByteDataUtils.toHex(Files.readAllBytes(Paths.get(cliParser.getOptionValue("toHexF")))));
        }

        if(cliParser.hasOption("enBase64")){
            System.out.println(Base64Utils.encode(cliParser.getOptionValue("enBase64")));
        }
        if(cliParser.hasOption("enBase64f")){
            System.out.println(Base64Utils.encode(Files.readAllBytes(Paths.get(cliParser.getOptionValue("enBase64f")))));
        }
        if(cliParser.hasOption("deBase64")){
            System.out.println(Base64Utils.decode(cliParser.getOptionValue("deBase64")));
        }
        if(cliParser.hasOption("deBase64f")){
            System.out.println(Base64Utils.decode(new String(Files.readAllBytes(Paths.get(cliParser.getOptionValue("deBase64f"))))));
        }
        if(cliParser.hasOption("enURL")){
           System.out.println(URLEncoder.encode(cliParser.getOptionValue("enURL"),"utf-8"));
        }
        if(cliParser.hasOption("deURL")){
            System.out.println(URLDecoder.decode(cliParser.getOptionValue("deURL"),"utf-8"));
        }
    }

}
