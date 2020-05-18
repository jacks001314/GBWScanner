#!/bin/bash
#
#      Filename: install.sh
#

INSTALL_DIR = /opt/scan
GBWScanner_DIR = GBWScanner
echo "Start to install GBWScanner..."

rm -rf /opt/scan

mkdir -p  $INSTALL_DIR

mkdir -p /opt/log/scan
chmod 777 /opt/log/scan


mv -f $GBWScanner_DIR $INSTALL_DIR/

chmod 755 /opt/scan/GBWScanner
echo "Install GBWScanner done..."
