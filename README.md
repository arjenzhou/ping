# ping

        _____                
    ___________(_)_____________ _
    ___  __ \_  /__  __ \_  __ `/
    __  /_/ /  / _  / / /  /_/ /
    _  .___//_/  /_/ /_/_\__, /  
    /_/                 /____/   

ping is a universal data source connection test project.

## How to use

| parameter | description | example | required |
| --- | --- | --- | --- |
| --type | datasource type | postgresql | yes | |
| --url | jdbc url | jdbc:postgresql://host:port/catalog | yes |
| --sql | select sql | SELECT 1 | yes |
| --username | | ping | false |
| --password | | pong | false |
| --keytabPath | keytab file path | ~/foo.keytab | false |
| --clientPrincipal | client principal | foo/bar@XAB.DE | false |
| --krb5Path | krb5.conf file path | /etc/krb5.conf | false |
| --driverFolder | custom driver folder | ~/drivers/custom/ | false |
| --driverClass | external driver class | /Users/foo/bar | false |

## Common Driver

### PostgreSQL

required parameters: type, url, sql, username, password
```
java -jar ping.jar --type postgresql --url "jdbc:postgresql://127.0.0.1:5432/postgres" --username postgres --password password --sql "SELECT 1"
```

### Redshift

required parameters: type, url, sql, username, password
```
java -jar ping.jar --type redshift --url "jdbc:redshift://127.0.0.1:5432/abc" --username foo --password bar --sql "SELECT 1"
```
### PrestoDB

required addon parameters which used to auth with Kerberos or LDAP

```
java -jar ping.jar --type prestodb --url "jdbc:presto://127.0.0.1:8081/hive/bi" --sql "SELECT 1"
```

```
java -jar ping.jar --type prestodb --username hive --url "jdbc:presto://127.0.0.1:7778?SSL=true&KerberosKeytabPath=./impala.keytab&KerberosPrincipal=impala/foo.org@BAR.COM&SSLTrustStorePath=./keystore.jks&SSLTrustStorePassword=password&KerberosRemoteServiceName=impala&KerberosConfigPath=./krb5.conf" --sql "SELECT 1"
```

### Trino

required addon parameters which used to auth with Kerberos or LDAP

```
java -jar ping.jar --type trino --url "jdbc:trino://127.0.0.1:8081/hive/bi" --sql "SELECT 1"
```

```
java -jar ping.jar --type trino --username hive --url "jdbc:trino://127.0.0.1:7778?SSL=true&KerberosKeytabPath=./impala.keytab&KerberosPrincipal=impala/foo.org@BAR.COM&SSLTrustStorePath=./keystore.jks&SSLTrustStorePassword=password&KerberosRemoteServiceName=impala&KerberosConfigPath=./krb5.conf" --sql "SELECT 1"
```

## Builtin Driver

ping also support builtin drivers, located in [drivers](src/main/resources/drivers)

### MySQL

Usage of mysql if something like common drivers. type, url, sql, username are required, password if needed

```
 java -jar ping.jar --type mysql --username foo --password bar --url "jdbc:mysql://127.0.0.1:3306/abc" --sql "SELECT 1"
```
### Hive

By default, krb5.conf is located at `/etc/krb5.conf`, and not required.

```
java -jar ping.jar --type hive --url "jdbc:hive2://127.0.0.1:21050/test;auth=noSasl" --sql "SELECT 1"
```

```
java -jar ping.jar  --type hive --url "jdbc:hive2://127.0.0.1:2181,127.0.0.2:2181,127.0.0.3:2181/foo;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2;principal=hive/_HOST@BAR" --sql "SELECT * FROM FOO.BAR" --keytabPath ./abc.keytab --clientPrincipal foo/bar@ABC.COM
```
### Impala

Impala is roughly same with Hive

```
java -jar ping.jar --url jdbc:impala://127.0.0.1:21050/;authType=noSasl --username foo --type impala --sql "SELECT 1"
```
```
java -jar ping.jar --type impala --url "jdbc:impala://127.0.0.1:21050/;AuthMech=1;KrbRealm=ABCD.COM;KrbHostFQDN=_HOST;KrbServiceName=impala;KrbAuthType=0" --sql "SELECT 1" --keytabPath foo.keytab --clientPrincipal foo/bar@ABC.COM --krb5Path krb5.conf
```

## External Driver

Using custom driver, `--driverFolder` and `--driverClass` is required to specify which directory to load drivers.

For example:
```
java -jar ping.jar --type external --username dbadmin --password dbadmin --url "jdbc:vertica://127.0.0.1:5433/vmart" --sql "SELECT * FROM "online_sales"."call_center_dimension" LIMIT 3" --driverFolder "path_to_folder" --driverClass "com.vertica.jdbc.Driver"
```
## How to add Connector

ping use SPI to load connectors, you can add your connector in your `META-INF/ping/service` file.