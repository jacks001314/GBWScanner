package com.gbw.scanner.elasticsearch;

import com.xmap.api.utils.TextUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ESStringQueryBuilder {

    private StringBuffer sb;

    public ESStringQueryBuilder(){

        sb = new StringBuffer();
    }

    public QueryBuilder build(){

        return QueryBuilders.queryStringQuery(sb.toString());
    }

    private void add(String name,Object value){

        if(sb.length()>0)
            sb.append(" AND ");

        sb.append("(");
        sb.append(name);
        sb.append(":");
        sb.append(value);
        sb.append(")");
    }

    public ESStringQueryBuilder field(String name,String value){

        if(TextUtils.isEmpty(value)|| TextUtils.isEmpty(name))
            return this;

        if(value.contains("/")){

            value = value.replace("/","\\/");
        }

        if(value.contains(":"))
        {
            value = value.replace(":","\\:");
        }

        add(name,value);
        return this;
    }
    public ESStringQueryBuilder field(String name,int value){

        if(value<0)
            return this;

        add(name,value);

        return this;
    }

    public ESStringQueryBuilder field(String name,long value){

        if(value<0)
            return this;

        add(name,value);
        return this;
    }

    public ESStringQueryBuilder field(String name,float value){


        add(name,value);
        return this;
    }

    public ESStringQueryBuilder field(String name,double value){

        add(name,value);
        return this;
    }

    public ESStringQueryBuilder append(String search){

        if(TextUtils.isEmpty(search))
            return this;

        sb.append(" AND ");
        sb.append(search);
        return this;
    }

    public ESStringQueryBuilder field(String name, ValueRange range){

        if(range == null)
            return this;

        long fromV = range.getFrom();
        long toV = range.getTo();

        if(fromV==0&&toV==0||fromV>toV)
            return this;

        if(sb.length()>0)
            sb.append(" AND ");

        sb.append("(");
        sb.append(name);
        sb.append(":");
        sb.append("[");

        if(fromV>=0){
            sb.append(fromV);
        }else{
            sb.append("*");
        }

        sb.append(" TO ");

        if(toV>0){
            sb.append(toV);
        }else{
            sb.append("*");
        }

        sb.append("]");
        sb.append(")");
        return this;
    }

}
