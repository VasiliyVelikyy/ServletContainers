mvn clean package

copy war in apache-tomcat-10.1.28/webapps


cd \apache-tomcat-10.1.28\bin
start - startup.bat
stop  - shutdown.bat


tomcat ui -http://localhost:8080/manager/html

GET http://localhost:8080/api/posts
POST http://localhost:8080/api/posts
DELETE http://localhost:8080/api/posts/1