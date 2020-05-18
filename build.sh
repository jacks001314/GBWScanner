#!/bin/bash

source /etc/profile
GBWScanner_SRC=GBWScanner
GBWScanner_TAR=GBWScanner-1.0-bin.tar.gz
GBWScanner_DIST=target/GBWScanner-1.0-bin.tar.gz
GBWScanner_JAR=target/GBWScanner-1.0.jar

rm -rf build target

mkdir build

mvn clean
mvn install -Dmaven.test.skip=true

cp $GBWScanner_DIST build/

cd build
tar -zxf $GBWScanner_TAR
cp ../$GBWScanner_JAR $GBWScanner_SRC/lib
cp ../install.sh ./
chmod a+x *.sh
#tar -czf $GBWScanner_TAR $SMARTEYE_SRC install.sh
#rm -rf $GBWScanner_SRC install.sh
rm -fr $GBWScanner_TAR

echo ""
echo "build done..."
