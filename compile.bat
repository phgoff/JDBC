@echo off

echo Compiling %* with UCanAccess...


javac  -cp "UCanAccess-4.0.2-bin\ucanaccess-4.0.2.jar;UCanAccess-4.0.2-bin\lib\*;."  %*

echo Finished.

