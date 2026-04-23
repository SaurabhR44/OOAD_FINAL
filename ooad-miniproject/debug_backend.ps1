$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
mvn spring-boot:run -DskipTests > backend_log.txt 2>&1
Type backend_log.txt
