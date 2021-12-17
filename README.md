# Log4Shell Honeypot

This demo application is vulnerable to the [CVE-2021-44228](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2021-44228),
also known as Log4Shell. For example, it can be used to validate a detection or remediation script.

## How to launch

Build the application:

    mvn clean package
    
Or, alternatively, download the [pre-built JAR](https://github.com/GroupePSA/log4shell-honeypot/releases/download/1.0.0-SNAPSHOT/log4shell-honeypot-capsule.jar).

Run the application (in the `target` subdirectory if you built it locally):

    java -Dlog4shell.all -jar log4shell-honeypot-capsule.jar

> The application is not vulnerable by default. You need to set the `log4shell.all` system property to enable 
> vulnerabilities (or you can use vulnerability-specific options, see below).

### Vulnerability options

* Set `log4shell.userAgent` system property to log the `User-Agent` HTTP header.
* Set `log4shell.authorization` system property to log the `Authorization` HTTP header.
* Set `log4shell.basicAuth` system property to log the user/password pair decoded from basic authentication.
* Set `log4shell.urlPath` system property to log the URL path.
* Set `log4shell.urlQuery` system property to log the URL query string.
* Set `log4shell.all` system property to log all the above.

An empty property value is enough.

### Security options

To enable basic authentication on the application, active the `basicAuth` configuration profile:

    java -Dseedstack.profiles=basicAuth -Dlog4shell.all -jar target/log4shell-honeypot-capsule.jar

* User is `demo`
* Password is also `demo`

> When basic authentication is enabled, the application cannot be vulnerable to the user/password injection.

## How to use

Do a GET or POST request on any path with a malicious payload located in accordance with the options above:

Example with `User-agent` header:

    curl http://localhost:8080 -A "<malicious-user-agent>"

The app will issue a 302 to `/test` which contains the vulnerability. 
