<a name="Top"></a>

CXF CLIENT
======

* <a href="#Introduction">Introduction</a>
* <a href="#Script">Wsdl2java Script</a>
* <a href="#Manually">Wsdl2java Manually</a>
* <a href="#Plugin">Plugin Configuration</a>
* <a href="#Mime">Mime Attachments</a>
* <a href="#Security">Custom Security Interceptors</a>
* <a href="#In">Custom In Interceptors</a>
* <a href="#Out">Custom Out Interceptors</a>
* <a href="#Fault">Custom Out Fault Interceptors</a>
* <a href="#Custom">Custom Http Client Policy</a>
* <a href="#Exceptions">Dealing With Exceptions</a>
* <a href="#Beans">User Client Beans Anywhere</a>
* <a href="#Endpoints">Retrieving and Updating Endpoints</a>
* <a href="#Demo">Demo Project</a>
* <a href="#Issues">Issues</a>
* <a href="#Change">Change Log</a>
* <a href="#Future">Future Revisions</a>
* <a href="#License">License</a>

<a name="Introduction"></a>
INTRODUCTION
---------------

The Grails Cxf plugin makes exposing services as SOAP endpoints easy and painless.  Since version 1.0.0, it was rewritten and enhanced to support more features including the migration to grails 2.0+.

<p align="right"><a href="#Top">Top</a></p>
<a name="Script"></a>

<a name="Script"></a>
WSDL2JAVA SCRIPT
---------------
Included with the plugin is a convenient script to generate java code from a wsdl.  Please note that the grails cxf-client also includes a similar script albeit using different configuration to create java files from Config.groovy instead of command line params.

```
usage: grails wsdl-2-java --wsdl=<path to wsdl> [--package=<package>]

Script Options:
  -h, --help           Prints this help message
  -p, --package=arg    The package to put the generated Java objects in.
  -w, --wsdl=arg       The path to the wsdl to use.

See <http://cxf.apache.org/docs/wsdl-to-java.html> for additional options.
```