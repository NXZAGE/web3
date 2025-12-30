# Lab 3

## Deploy

### 1. psql driver

download

module.xml

add driver:

```shell
  /subsystem=datasources/jdbc-driver=postgresql:add(
   driver-name=postgresql,
   driver-module-name=org.postgresql,
   driver-class-name=org.postgresql.Driver
  )
```

### 2. data source

```shell
/subsystem=datasources/data-source=PostgresDS:add(jndi-name=java:/jdbc/PostgresDS,driver-name=postgresql,connection-url=jdbc:postgresql://localhost:5432/studs,user-name=s466828, password=S420iSC5emrFnJ1Q, enabled=true )
```