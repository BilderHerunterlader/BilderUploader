::Version in Datei schreiben
java -jar BilderUploader.jar -versionNumber > vy.txt
java -jar BilderUploader.jar -version > vz.txt

::Datei einlesen
for /f "tokens=1" %%i in (vy.txt) do set version=%%i
for /f "tokens=1" %%o in (vz.txt) do set versionx=%%o
::Dateie loeschen
del vy.txt
del vz.txt

IF EXIST Setup\BU%version%Setup.nsi GOTO compile

:template
setup\sed\sed.exe "s/TemplateY/%versionx%/g" Setup\BUTemplateSetup.nsi >> BUTemplateSetupX.nsi
setup\sed\sed.exe "s/TemplateX/%version%/g" BUTemplateSetupX.nsi >> Setup\BU%version%Setup.nsi
del BUTemplateSetupX.nsi

:compile
if exist "%PROGRAMFILES(X86)%\NSIS\makensisw.exe" goto x86

"%programfiles%\NSIS\makensisw.exe" Setup\BU%version%Setup.nsi
goto continue

:x86
"%PROGRAMFILES(X86)%\NSIS\makensisw.exe" Setup\BU%version%Setup.nsi

:continue

del Setup\BU%version%Setup.nsi

move Setup\BilderUploader%version%Setup.exe .
