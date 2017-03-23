# geowe-geometry-service
A rest service that allows to perform geometric operations

The **geowe-geometry-service**, in advance **GGS**, is being developed using the following technologies:

- [jboss RESTEasy](http://resteasy.jboss.org/). a fully certified and portable implementation of the JAX-RS 2.0 specification, a JCP specification that provides a Java API for RESTful Web Services over the HTTP protocol
- The **JTS Topology Suite** is an API for modelling and manipulating 2-dimensional linear geometry. It provides numerous geometric predicates and functions. JTS conforms to the **Simple Features Specification** for SQL published by the **Open GIS Consortium**
- JDK 8

##Features

- Geometry Operations: buffer, centroid, envelope, intersection, intersects, union, ...

##License

The **GGS** is licensed under the [Apache License v2](https://www.apache.org/licenses/LICENSE-2.0), meaning you can use it free of charge, according with license terms and conditions.

##Build the software
In order to compile and build **GGS**, the JDK 8 platform is necessary. The project uses maven for building and packaging.
Configure the pom.xml in order to use your jdk path and execute:
	
	mvn clean package

##Deploy
Once you compiled the software, the ggs.war file can be deployed on any server/application container, like Apache Tomcat. It would be great to have multiple profiles in the pom.xml file to compile and deploy to other application servers. Help us!

##Testing
**GGS** is being tested on Apache Tomcat 8.5. Once deployed you can execute the unit tests included in the project. You can also run tests designed for jmeter included in this repository

##API
You can see api's documentation under development in the directory [api](https://github.com/i62lotor/geowe-geometry-service/tree/master/api)
