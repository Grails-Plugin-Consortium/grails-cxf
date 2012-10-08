<html>
<head>
  <title>Welcome to Grails</title>
  <meta name="layout" content="main"/>
  <style type="text/css" media="screen">

  #nav {
    margin-top: 20px;
    margin-left: 30px;
    width: 228px;
    float: left;

  }

  .homePagePanel * {
    margin: 0px;
  }

  .homePagePanel .panelBody ul {
    list-style-type: none;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody h1 {
    text-transform: uppercase;
    font-size: 1.1em;
    margin-bottom: 10px;
  }

  .homePagePanel .panelBody {
    background: url(images/leftnav_midstretch.png) repeat-y top;
    margin: 0px;
    padding: 15px;
  }

  .homePagePanel .panelBtm {
    background: url(images/leftnav_btm.png) no-repeat top;
    height: 20px;
    margin: 0px;
  }

  .homePagePanel .panelTop {
    background: url(images/leftnav_top.png) no-repeat top;
    height: 11px;
    margin: 0px;
  }

  h2 {
    margin-top: 15px;
    margin-bottom: 15px;
    font-size: 1.2em;
  }

  #pageBody {
    margin-left: 280px;
    margin-right: 20px;
  }
  </style>
</head>

<body>
<div id="nav">
  <div class="homePagePanel">
    <div class="panelTop"></div>

    <div class="panelBody">
      <h1>Application Status</h1>
      <ul>
        <li>App version: <g:meta name="app.version"></g:meta></li>
        <li>Grails version: <g:meta name="app.grails.version"></g:meta></li>
        <li>Groovy version: ${org.codehaus.groovy.runtime.InvokerHelper.getVersion()}</li>
        <li>JVM version: ${System.getProperty('java.version')}</li>
        <li>Controllers: ${grailsApplication.controllerClasses.size()}</li>
        <li>Domains: ${grailsApplication.domainClasses.size()}</li>
        <li>Services: ${grailsApplication.serviceClasses.size()}</li>
        <li>Tag Libraries: ${grailsApplication.tagLibClasses.size()}</li>
      </ul>

      <h1>Installed Plugins</h1>
      <ul>
        <g:set var="pluginManager"
               value="${applicationContext.getBean('pluginManager')}"></g:set>

        <g:each var="plugin" in="${pluginManager.allPlugins}">
          <li>${plugin.name} - ${plugin.version}</li>
        </g:each>

      </ul>
    </div>

    <div class="panelBtm"></div>
  </div>
</div>

<div id="pageBody">
  <h1>Grails CXF Plugin</h1>

  <p>If you are running this, you have downloaded the source.  Here are the configured service endpoints:</p>

  <div id="servletList" class="dialog">
    <h2>Available CXF Servlets:</h2>
    <ul>
      <g:each var="c" in="${org.grails.cxf.utils.GrailsCxfUtils.getServletsMappings()}">
        <li class="controller"><g:link uri="${c?.value?.toString()?.replace('*','')}">${c?.key}</g:link></li>
      </g:each>
    </ul>
  </div>

  <div id="endpointList" class="dialog">
    <h2>Available Endpoints:</h2>
    <ul>
      <g:each var="c" in="${grailsApplication.endpointClasses.sort { it.fullName }}">
        <li class="controller"><g:link uri="/services${c.address}?wsdl">${c.fullName}</g:link></li>
      </g:each>
    </ul>
  </div>

  <div id="serviceList" class="dialog">
    <h2>Available Services</h2>
   <p>Currently the services are not iterable as they share the base artefact type of "Service" with grails.  If we move away from this,
   the list of services exposed will be added similar to the endpoints above.</p>
  </div>

  <div id="controllerList" class="dialog">
    <h2>Available Controllers:</h2>
    <ul>
      <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName }}">
        <li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
      </g:each>
    </ul>
  </div>
</div>
</body>
</html>
