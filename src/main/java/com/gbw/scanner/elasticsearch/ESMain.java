package com.gbw.scanner.elasticsearch;

import com.gbw.scanner.utils.ESUtil;
import org.apache.commons.cli.*;
import org.elasticsearch.client.Client;

import java.util.List;

public class ESMain {


    private static void esCount(ESService esService,String query) {

        System.out.println(esService.count(query));
    }

    private static void esTop(ESService esService,String query,String args){

        String[] splits = args.split(":");
        String field = splits[0];
        int topN = Integer.parseInt(splits[1]);
        boolean isansc = splits[2].toLowerCase().equals("true");

        List<AggItem> results = esService.topBYTerm(query,field,topN,isansc);

        results.forEach(e->System.out.println(e));

    }

    private static void esTop2(ESService esService,String query,String args){

        String[] splits = args.split(":");
        String field1 = splits[0];
        String field2 = splits[1];
        int topN = Integer.parseInt(splits[2]);
        boolean isansc = splits[3].toLowerCase().equals("true");

        List<MoreAggItem> results = esService.topBYTerms2(query,topN,isansc,field1,field2);

        results.forEach(e->System.out.println(e));

    }

    private static void esMax(ESService service,String query,String field){

        System.out.println(service.maxValue(query,field));
    }

    private static void esMin(ESService service,String query,String field){

        System.out.println(service.minValue(query,field));
    }

    private static void esSum(ESService service,String query,String field){

        System.out.println(service.sumValue(query,field));
    }

    private static void esIndices(ESIndexService indexService,String ind){

        for(String index:indexService.getIndices(ind)){
            System.out.println(index);
        }
    }

    private static void esOpen(ESIndexService esIndexService,String index){

        esIndexService.openIndex(index);
    }

    private static void esClose(ESIndexService indexService,String index){

        indexService.closeIndex(index);

    }

    public static void main(String[] args) throws Exception {

        ESService esService;
        ESIndexService indexService;

        String cluster = "antell-SmartEye-ES";
        String host = "127.0.0.1";
        int port = 9300;
        String index = "log_*";
        String doc = "esdatabase_doc";

        String queryString = "";

        Options opts = new Options();

        opts.addOption("host", true, "es address:<ip>");
        opts.addOption("port", true, "es port ");
        opts.addOption("cluster", true, "es cluster name ");
        opts.addOption("index", true, "es index ");
        opts.addOption("doc", true, "es doc ");

        opts.addOption("query", true, "es query string");


        opts.addOption("count", false, "count cmd");
        opts.addOption("top", true, "topN cmd by one term args: <field>:<topN><isasc>");
        opts.addOption("top2", true, "topN cmd by two term args: <field1>:<field2>:<topN><isasc>");
        opts.addOption("max", true, "get a field max value ,args: <field>");
        opts.addOption("min", true, "get a field min value ,args: <field>");
        opts.addOption("sum", true, "get a field sum value ,args: <field>");

        opts.addOption("indices", false, "get all es indices");
        opts.addOption("close",true,"close es index,args:<index>");
        opts.addOption("open",true,"open es index,args:<index>");

        opts.addOption("help", false, "Print usage");

        CommandLine cliParser = new GnuParser().parse(opts, args);
        if(cliParser.hasOption("help")){
            new HelpFormatter().printHelp("es-tool", opts);
            System.exit(0);
        }

        if(cliParser.hasOption("host"))
            host = cliParser.getOptionValue("host");

        if(cliParser.hasOption("port"))
            port = Integer.parseInt(cliParser.getOptionValue("port"));

        if(cliParser.hasOption("index"))
            index = cliParser.getOptionValue("index");

        if(cliParser.hasOption("doc"))
            doc = cliParser.getOptionValue("doc");

        if(cliParser.hasOption("cluster"))
            cluster = cliParser.getOptionValue("cluster");

        Client client = ESUtil.getClient(cluster,host,port);

        esService = new ESService(client,index,doc);
        indexService = new ESIndexService(client);

        if(cliParser.hasOption("query"))
            queryString = cliParser.getOptionValue("query");

        if(cliParser.hasOption("count")){
            esCount(esService,queryString);
        }

        if(cliParser.hasOption("top")){

            esTop(esService,queryString,cliParser.getOptionValue("top"));
        }

        if(cliParser.hasOption("top2")){

            esTop2(esService,queryString,cliParser.getOptionValue("top2"));
        }

        if(cliParser.hasOption("max")){

            esMax(esService,queryString,cliParser.getOptionValue("max"));
        }

        if(cliParser.hasOption("min")){

            esMin(esService,queryString,cliParser.getOptionValue("min"));
        }

        if(cliParser.hasOption("sum")){

            esSum(esService,queryString,cliParser.getOptionValue("sum"));
        }

        if(cliParser.hasOption("indices")){

            esIndices(indexService,index);
        }

        if(cliParser.hasOption("open")){
            esOpen(indexService,cliParser.getOptionValue("open"));
        }

        if(cliParser.hasOption("close")){
            esClose(indexService,cliParser.getOptionValue("close"));
        }


        client.close();

    }


}
