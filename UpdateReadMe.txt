Update / Add Library:
- Update dependency version or add dependency in pom.xml
If a Library requires to display a specific license text then it need to be added to: src\main\resources\Application_Library_Licenses.properties

Update BH:
- Update Version in src\main\resources\Application_Config.properties
- mvn release:prepare release:perform
- Execute createBinaryZipFile.bat, createJarSetupFile.bat and createNSISSetupFile.bat and move the created files to a directory outside of the project
- Execute createSourceZipFile.bat and move the created files to a directory outside of the project
