#!/bin/bash
#
#      Filename: install.sh
#

INSTALL_DIR=/opt/scan
GBWScanner_DIR=GBWScanner
echo "Start to install GBWScanner..."

rm -rf /opt/scan
rm -rf /opt/data/script/jclass/
mkdir -p  $INSTALL_DIR
mkdir -p /opt/data/script/jclass
mkdir -p /opt/log/scan
chmod 777 /opt/log/scan


mv -f $GBWScanner_DIR $INSTALL_DIR/
mv -f JarMain.class /opt/data/script/jclass/
chmod 755 /opt/scan/GBWScanner
chmod a+xwr /opt/scan -R
chown server:server /opt/scan -R

echo "Install GBWScanner done..."
