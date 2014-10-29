url-resolving-demo
==================

Servlet 3.0 example that demonstrates the difference of resource URL resolving on different servlet containers

[ResourcesServlet](src/main/java/demo/ResourcesServlet.java) is the example servlet.

building war file with wrapped gradle version:
```
./gradlew war
cp build/libs/url-resolving-demo.war /your/servlet_container/webapps/
```

testing with 
```
curl http://localhost:8080/url-resolving-demo/get
curl http://localhost:8080/url-resolving-demo/get?resource=hello.txt
```
