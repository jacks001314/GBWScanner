package com.gbw.scanner.rule;

import com.xmap.api.utils.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GBWRuleMatch {

    private GBWRuleMatch(){

    }

    private interface OPAction {

        boolean isMatch(String tvalue,String value,boolean isArray);
    }


    private static Map<String,OPAction> opMaps = new HashMap<>();

    static {

        opMaps.put(GBWRuleConstants.contains, (tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(tvalue.contains(s))
                        return true;
                }
                return false;

            }
            return tvalue.contains(value);
        });

        opMaps.put(GBWRuleConstants.startsWith,(tvalue, value, isArray) -> {
            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(tvalue.startsWith(s))
                        return true;
                }
                return false;
            }
            return tvalue.startsWith(value);
        });

        opMaps.put(GBWRuleConstants.endsWith,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(tvalue.endsWith(s))
                        return true;
                }
                return false;
            }
            return tvalue.endsWith(value);
        });

        opMaps.put(GBWRuleConstants.regex,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    Pattern pattern = Pattern.compile(s);
                    Matcher matcher = pattern.matcher(tvalue);
                    if(matcher.matches())
                        return true;
                }
                return false;
            }

            Pattern pattern = Pattern.compile(value);
            Matcher matcher = pattern.matcher(tvalue);
            return matcher.matches();
        });

        opMaps.put(GBWRuleConstants.eq,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(tvalue.equals(s))
                        return true;
                }
                return false;
            }

            return tvalue.equals(value);
        });

        opMaps.put(GBWRuleConstants.lt,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(Integer.parseInt(tvalue)<Integer.parseInt(s))
                        return true;
                }
                return false;
            }

            return Integer.parseInt(tvalue)<Integer.parseInt(value);
        });

        opMaps.put(GBWRuleConstants.gt,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(Integer.parseInt(tvalue)>Integer.parseInt(s))
                        return true;
                }
                return false;
            }

            return Integer.parseInt(tvalue)>Integer.parseInt(value);
        });

        opMaps.put(GBWRuleConstants.le,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(Integer.parseInt(tvalue)<=Integer.parseInt(s))
                        return true;
                }
                return false;
            }

            return Integer.parseInt(tvalue)<=Integer.parseInt(value);
        });
        opMaps.put(GBWRuleConstants.ge,(tvalue, value, isArray) -> {

            if(isArray){

                for(String s:value.split(GBWRuleConstants.arrSplit)){

                    if(Integer.parseInt(tvalue)>=Integer.parseInt(s))
                        return true;
                }
                return false;
            }

            return Integer.parseInt(tvalue)>=Integer.parseInt(value);
        });

    }

    private static boolean opMatch(String tvalue,String op,String value,boolean isArray){

        if(TextUtils.isEmpty(tvalue)|| TextUtils.isEmpty(op)|| TextUtils.isEmpty(value))
            return false;

        OPAction action = opMaps.get(op);
        if(action == null)
            return false;

        return action.isMatch(tvalue,value,isArray);
    }

    private static boolean doMatch(GBWRuleSourceEntry sourceEntry, GBWRuleItem ruleItem){

        String tvalue = sourceEntry.getTargetValue(ruleItem);

        boolean res = opMatch(tvalue,ruleItem.getOp(),ruleItem.getValue(),ruleItem.isArray());

        return ruleItem.isIsnot()?!res:res;
    }

    public static boolean isMatch(GBWRuleSourceEntry sourceEntry, GBWRule rule){

        boolean isAnd = rule.isAnd();

        List<GBWRuleItem> ruleItems = rule.getItems();

        for(GBWRuleItem item:ruleItems){

            boolean res = doMatch(sourceEntry,item);

            if(isAnd){
                if(!res)
                    return false;
            }else{
                if(res)
                    return true;
            }
        }

        return isAnd;
    }

}
