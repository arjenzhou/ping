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

## Builtin Driver

ping have some builtin drivers

### PostgreSQL

required parameters: type, url, sql, username, password

## Custom Driver

ping also support external drivers, located in [drivers](src/main/resources/drivers)

### Hive

By default, krb5.conf is located at `/etc/krb5.conf`, and not required.

### Impala

Impala is roughly same with Hive

### Custom

Using custom driver, `--driverFolder` is required to specify which directory to load drivers.

## How to add Connector

ping use SPI to load connectors, you can add your connector in your `META-INF/ping/service` file.