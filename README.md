jreadings
=========
 
A Device readings app in Java with RESTEasy for REST APIs and EclipseLink for ORM with PostgreSQL database.
It is meant for deployment on Openshift. This application's only assumption is the presence of a Data source
that does the connection pooling. So, as long as a servlet container has it defined, no modifications are needed
to run it on any other Servlet container.

### Device readings schema

Devices and their readings are defined as:

```
CREATE TABLE device_types (
  id SERIAL PRIMARY KEY,
  name varchar(256) NOT NULL,
  version varchar(256) NOT NULL
);

CREATE TABLE devices (
  id SERIAL PRIMARY KEY,
  mac_addr char(12) NOT NULL UNIQUE,
  device_type_id integer NOT NULL,
  manufactured_at timestamp NOT NULL,
  registered_at timestamp,
  FOREIGN KEY (device_type_id) REFERENCES device_types (id)
);

CREATE TABLE readings (
  id SERIAL PRIMARY KEY,
  device_mac_addr char(12) NOT NULL,
  value text NOT NULL,
  created_at timestamp NOT NULL,
  FOREIGN KEY (device_mac_addr) REFERENCES devices (mac_addr)
);
```

Readings are deliberately defined as strings to suport multiple kind of readings.

### APIs

* /api/1/devices
* /api/1/readings
* /api/1/devices/:device-id
* /api/1/devices/:device-id/readings?from=YYYY-mm-dd&to=YYYY-mm-dd


### Deployment

Just copy the src folder and pom.xml to the JBoss EWS - Tomcat 7 application created by Openshift.


### connection pooling (applicable, if you want to test it on a local servlet container)

This application assumes that the servlet container has a connection pooling enabled for a PostgreSQL database 
with a data source named 'jdbc/jreadings'.  This is the default datasource defined by Openshift JBOSS EWS (or Tomcat)
project for PostgreSQL cartridge. To run the application in the local servlet container, just put it in
$CATALINA_HOME/conf/context.xml with appropriate database authentication.

```
<?xml version="1.0" encoding="UTF-8"?>
<Context>
    <!-- Specify a JDBC datasource -->
    <Resource name="jdbc/jreadings" auth="Container" type="javax.sql.DataSource"
        maxActive="20" maxIdle="5" maxWait="10000"
        username="<db user>" password="<db password>" driverClassName="org.postgresql.Driver"
        url="jdbc:postgresql:<db name>" />
</Context>
```
