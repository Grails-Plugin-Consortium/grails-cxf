import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('Init')
includeTargets << grailsScript('_GrailsCreateArtifacts')

USAGE = """
	Usage: grails create-cxf-service-simple <endpoint-name>

	Creates a new Cxf Service wired with 'simple' service type

	Example: grails create-cxf-service-simple com.yourapp.SimpleService
"""

target(main: "Creates a new Cxf Service for the Cxf Plugin.") {
    depends(checkVersion, parseArguments)

    def type = "Service"
    promptForName(type: type)
    def name = purgeRedundantArtifactSuffix(argsMap["params"][0], type)

    createArtifact(name: name, suffix: type, type: "EndpointSimple", path: "grails-app/services")
}

purgeRedundantArtifactSuffix = { name, suffix ->
    if (name && suffix) {
        if (name =~ /.+$suffix$/) {
            name = name.replaceAll(/$suffix$/, "")
        }
    }
    name
}

setDefaultTarget(main)
