package com.gbw.scanner.plugins.bruteforce;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GBWDict {

    protected final List<GBWDictEntry> dictEntries;

    public GBWDict(String usersFname,String passwdsFname) throws IOException, InterruptedException {

        Preconditions.checkArgument(Files.exists(Paths.get(usersFname)),"User File:"+usersFname+" is not existed!");
        Preconditions.checkArgument(Files.exists(Paths.get(passwdsFname)),"Passwd File:"+passwdsFname+" is not existed!");

        Set<String> users = loadFile(usersFname);
        Set<String> passwds = loadFile(passwdsFname);

        dictEntries = new ArrayList<>();

        for(String user:users){

            for(String passwd:passwds){

                dictEntries.add(new GBWDictEntry(user,passwd));
            }
        }
    }

    public GBWDict(String passwdsFname) throws IOException, InterruptedException {

        Preconditions.checkArgument(Files.exists(Paths.get(passwdsFname)),"Passwd File:"+passwdsFname+" is not existed!");

        Set<String> passwds = loadFile(passwdsFname);

        dictEntries = new ArrayList<>();

        for(String passwd:passwds){
            dictEntries.add(new GBWDictEntry("any",passwd));
        }
    }

    private static Set<String> loadFile(String fname) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(fname));

        return lines.stream().filter(e->!e.isEmpty()).collect(Collectors.toSet());
    }

    public GBWDictEntry get(int index){

        if(index>=dictEntries.size())
            return null;

        return dictEntries.get(index);
    }

    public List<GBWDictEntry> getDictEntries() {
        return dictEntries;
    }
}
