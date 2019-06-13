@ECHO OFF

::Version in Datei schreiben
java -jar BilderUploader.jar -versionNumber > v.txt

::Datei einlesen
for /f "tokens=1" %%i in (v.txt) do set version=%%i
::Dateie loeschen
del v.txt

::version einsetzen und zip datei erstellen
"%programfiles%\7-zip\7z.exe" a -xr@excludelist.txt -tzip BilderUploader%version%Binary.zip hosts\*.* -r
"%programfiles%\7-zip\7z.exe" a -tzip BilderUploader%version%Binary.zip lib\*.jar -r
"%programfiles%\7-zip\7z.exe" a -xr@excludelist.txt -tzip BilderUploader%version%Binary.zip templates\* -r
"%programfiles%\7-zip\7z.exe" a -tzip BilderUploader%version%Binary.zip BilderUploader.jar
"%programfiles%\7-zip\7z.exe" a -tzip BilderUploader%version%Binary.zip %~dp0\BUIcons\Icon\BUIcon.ico
"%programfiles%\7-zip\7z.exe" a -tzip BilderUploader%version%Binary.zip LICENSE
"%programfiles%\7-zip\7z.exe" a -tzip BilderUploader%version%Binary.zip directories.properties.example
