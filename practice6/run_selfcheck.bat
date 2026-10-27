@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac --release 17 -encoding UTF-8 -d out src\ru\edu\pr03\*.java
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out ru.edu.pr03.SelfCheck
exit /b %errorlevel%
