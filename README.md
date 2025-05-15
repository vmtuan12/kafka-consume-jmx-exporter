Consuming message from kafka, transforming -> exporting jmx metrics

Build
```
mvn package
```

Run
```
java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -javaagent:jmx_prometheus_javaagent-1.2.0.jar=0.0.0.0:3614:jmx-config.yaml -jar export-jmx-1.0-SNAPSHOT.jar
```
