@echo off
rem Usage: call ..\common-build.bat <mod-dir> <jar-name>
rem Needs the game JAR (OPENVOXEL_GAME_JAR) and the loader JAR (OPENVOXEL_MODLOADER_JAR or ..\ModLoader-Client\dist\*.jar).
setlocal
set "MOD=%~1"
set "JARNAME=%~2"
set "ROOT=%~dp0."
set "GAME=%OPENVOXEL_GAME_JAR%"
if "%GAME%"=="" if exist "%ROOT%\..\..\..\Game\versions\0.0.8-Debug\openvoxel-0.0.8-Debug.jar" set "GAME=%ROOT%\..\..\..\Game\versions\0.0.8-Debug\openvoxel-0.0.8-Debug.jar"
if "%GAME%"=="" if exist "%ROOT%\..\ModLoader-Client\game\openvoxel.jar" set "GAME=%ROOT%\..\ModLoader-Client\game\openvoxel.jar"
if "%GAME%"=="" ( echo Game JAR not found; set OPENVOXEL_GAME_JAR. & exit /b 1 )
set "LOADER=%OPENVOXEL_MODLOADER_JAR%"
if "%LOADER%"=="" for %%j in ("%ROOT%\..\ModLoader-Client\dist\ModLoader-v*-vgame-0.0.8-client.jar") do set "LOADER=%%~fj"
if not exist "%LOADER%" ( echo Loader JAR not found; set OPENVOXEL_MODLOADER_JAR or build ..\ModLoader-Client. & exit /b 1 )
if exist "%MOD%\build" rmdir /s /q "%MOD%\build"
mkdir "%MOD%\build"
if not exist "%ROOT%\dist" mkdir "%ROOT%\dist"
dir /s /b "%MOD%\src\*.java" > "%MOD%\build\sources.txt"
javac --release 11 -encoding UTF-8 -Xlint:-options -classpath "%GAME%;%LOADER%" -d "%MOD%\build" @"%MOD%\build\sources.txt"
if errorlevel 1 exit /b 1
del "%MOD%\build\sources.txt"
if exist "%MOD%\resources" xcopy /e /i /y /q "%MOD%\resources" "%MOD%\build" >nul
if exist "%MOD%\src\main\resources" xcopy /e /i /y /q "%MOD%\src\main\resources" "%MOD%\build" >nul
jar --create --file "%ROOT%\dist\%JARNAME%" -C "%MOD%\build" .
if errorlevel 1 exit /b 1
if exist "%ROOT%\..\ModLoader-Client\instance\mods" copy /y "%ROOT%\dist\%JARNAME%" "%ROOT%\..\ModLoader-Client\instance\mods\" >nul
echo %JARNAME% -^> dist ^(and ..\ModLoader-Client\instance\mods if present^)
