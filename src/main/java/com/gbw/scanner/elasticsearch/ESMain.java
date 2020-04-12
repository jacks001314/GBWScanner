package com.gbw.scanner.elasticsearch;

import com.gbw.scanner.utils.ESUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

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

    private static void esSearch(ESService service,String q,int page,int pageSize,String sortField,boolean isDec,boolean isPretty){

        SearchHit[] hits = service.search(q,page,pageSize,sortField,isDec);

        Gson gson = isPretty?new GsonBuilder().setPrettyPrinting().create():new GsonBuilder().create();

        for(SearchHit hit:hits){

            System.out.println(gson.toJson(hit.getSourceAsMap()));
        }
    }

    private static void esDeleteByQuery(ESService service,String q){

        System.out.println(service.delete(q));
    }

    private static void esOpen(ESIndexService esIndexService,String index){

        esIndexService.openIndex(index);
    }

    private static void esClose(ESIndexService indexService,String index){

        indexService.closeIndex(index);

    }

    private static void esCloseBeforeDays(ESIndexService indexService,String args){

        String[] splits = args.split(":");

        indexService.closeIndexBeforeDay(splits[0],Integer.parseInt(splits[1]));

    }


    private static void esDel(ESIndexService indexService,String index){
        indexService.deleteIndex(index);
    }

    private static void esMapping(String host,int port,String index) throws Exception {

        System.out.println(ESHttpService.getMappings(host,port,index));
    }

    private static void esHttpGet(String host,int port,String uri) throws Exception{

        System.out.println(ESHttpService.get(host,port,uri));
    }

    private static void esHttpPost(String host,int port,String args) throws Exception{

        String[] splits = args.split(":");

        System.out.println(ESHttpService.post(host,port,splits[0],splits[2],splits[1].toLowerCase().equals("true")));


    }

    private static void esHttpQuery(String host,int port,String index,String doc,String query) throws Exception{

        String uri =  String.format("/%s/%s/_search?pretty=true",index,doc);
        String content = String.format(" {\"query\":{\"query_string\":{\"query\":%s}}}",query);

        System.out.println(ESHttpService.post(host,port,uri,content,false));

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
        int page = 0;
        int pageSize = 10;
        String sortField = "";
        boolean isDec = false;
        boolean isPretty = false;

        Options opts = new Options();

        opts.addOption("host", true, "es address:<ip>");
        opts.addOption("port", true, "es port ");
        opts.addOption("cluster", true, "es cluster name ");
        opts.addOption("index", true, "es index ");
        opts.addOption("doc", true, "es doc ");

        opts.addOption("query", true, "es query string");
        opts.addOption("page", true, "es search page");
        opts.addOption("pageSize", true, "es search pageSize");
        opts.addOption("sortField", true, "es search sort field");
        opts.addOption("sortOrderDec", false, "es search sort by dec order");
        opts.addOption("pretty", false, "es search result is pretty");

        opts.addOption("count", false, "count cmd");
        opts.addOption("top", true, "topN cmd by one term args: <field>:<topN><isasc>");
        opts.addOption("top2", true, "topN cmd by two term args: <field1>:<field2>:<topN><isasc>");
        opts.addOption("max", true, "get a field max value ,args: <field>");
        opts.addOption("min", true, "get a field min value ,args: <field>");
        opts.addOption("sum", true, "get a field sum value ,args: <field>");
        opts.addOption("search", false, "search es");
        opts.addOption("delByQuery", false, "delete es by query");

        opts.addOption("indices", false, "get all es indices");
        opts.addOption("close",true,"close es index,args:<index>");
        opts.addOption("closeBeforeDay",true,"close es indices before days,args:<index>:<before days>");
        opts.addOption("open",true,"open es index,args:<index>");
        opts.addOption("del",true,"delete es index,args:<index>");
        //opts.addOption("indices");
        opts.addOption("mapping",true,"get es index mappings,args:<index>");
        opts.addOption("hget",true,"get some es information by http get requst,args:<url>");
        opts.addOption("hpost",true,"get some es information by http post requst,args:<url>:<isFromFile>:<content>");
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

        opts.addOption("page", true, "es search page");
        opts.addOption("pageSize", true, "es search pageSize");
        opts.addOption("sortField", true, "es search sort field");
        opts.addOption("sortOrderDec", false, "es search sort by dec order");
        if(cliParser.hasOption("page"))
            page = Integer.parseInt(cliParser.getOptionValue("page"));

        if(cliParser.hasOption("pageSize"))
            pageSize = Integer.parseInt(cliParser.getOptionValue("pageSize"));
        if(cliParser.hasOption("sortField"))
            sortField = cliParser.getOptionValue("sortField");

        if(cliParser.hasOption("sortOrderDec"))
            isDec = true;

        if(cliParser.hasOption("pretty"))
            isPretty = true;

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

        if(cliParser.hasOption("closeBeforeDay")){

            esCloseBeforeDays(indexService,cliParser.getOptionValue("closeBeforeDay"));
        }

        if(cliParser.hasOption("del")){

            esDel(indexService,cliParser.getOptionValue("del"));
        }

        if(cliParser.hasOption("mapping")){

            esMapping(host,port,cliParser.getOptionValue("mapping"));
        }

        if(cliParser.hasOption("hget")){

            esHttpGet(host,port,cliParser.getOptionValue("hget"));
        }

        if(cliParser.hasOption("hpost")){

            esHttpPost(host,port,cliParser.getOptionValue("hpost"));
        }

        if(cliParser.hasOption("search")){

            esSearch(esService,queryString,page,pageSize,sortField,isDec,isPretty);
        }

        if(cliParser.hasOption("delByQuery")){

            esDeleteByQuery(esService,queryString);
        }

        client.close();

    }


}
