import json
import sys

if len(sys.argv)<3:
    print('Usage:tool.py <fpath> <outFile>')
    sys.exit(-1)

fpath = sys.argv[1]
outFpath = sys.argv[2]
fd = open(fpath)
writer = open(outFpath,"w+")

lines = fd.readlines()

for s in lines:
    obj = json.loads(s)
    host = obj['host']
    port = obj['port']
    writer.write(host+":"+str(port)+"\n")

fd.close()
writer.close()
