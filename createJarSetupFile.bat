::Version in Datei schreiben
java -jar BilderUploader.jar -versionNumber > vy.txt
java -jar BilderUploader.jar -version > vz.txt

::Datei einlesen
for /f "tokens=1" %%i in (vy.txt) do set version=%%i
for /f "tokens=1" %%o in (vz.txt) do set versionx=%%o
::Dateie loeschen
del vy.txt
del vz.txt

:template
del Setup\izpack\install.xml
setup\sed\sed.exe "s/TemplateY/%versionx%/g" Setup\izpack\template_install.xml >> Setup\izpack\install.xml

:compile
cd Setup\izpack
call "%programfiles%\IzPack\bin\compile" install.xml -b . -o BilderUploader%version%Setup.jar -k standard

cd ..\..

del Setup\izpack\install.xml

move Setup\izpack\BilderUploader%version%Setup.jar .
