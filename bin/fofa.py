#!/bin/python

import json
import sys


if len(sys.argv)<3:
    print("Usage: fofa.py <json file> <outfile>")
    sys.exit(-1)

rfd = open(sys.argv[1],"r")
wfd = open(sys.argv[2],"w+")

for entry in json.load(rfd):
    wfd.write(entry[0]+"\n")

rfd.close()
wfd.close()
