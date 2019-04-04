CICB 5.5.0

CD ${project_location}
run "mvn install:install-file -Dfile=libs\\ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar"
to start server run 
- mvnw
to start client
- yarn start
