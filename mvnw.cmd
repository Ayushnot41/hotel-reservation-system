@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Begin all REM://maven.apache.org/download.cgi
@REM Maven Wrapper script for Windows

@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_CMD_LINE_ARGS=%*

@REM Find java.exe
set JAVA_EXE=java

@REM Download Maven if not present
set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
set MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd

if exist "%MAVEN_BIN%" (
    "%MAVEN_BIN%" %MAVEN_CMD_LINE_ARGS%
    goto end
)

@REM Download Maven
echo Downloading Apache Maven 3.9.6...
mkdir "%MAVEN_HOME%" 2>nul

powershell -Command "& { Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile '%TEMP%\maven.zip' -UseBasicParsing }"
powershell -Command "& { Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%USERPROFILE%\.m2\wrapper\dists' -Force }"

if exist "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd" (
    "%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6\bin\mvn.cmd" %MAVEN_CMD_LINE_ARGS%
) else (
    @REM Try to find it in extracted directory
    for /d %%i in ("%USERPROFILE%\.m2\wrapper\dists\apache-maven-*") do (
        if exist "%%i\bin\mvn.cmd" (
            "%%i\bin\mvn.cmd" %MAVEN_CMD_LINE_ARGS%
            goto end
        )
    )
    echo ERROR: Could not set up Maven. Please install Maven manually.
    echo Download from: https://maven.apache.org/download.cgi
    exit /b 1
)

:end
endlocal
