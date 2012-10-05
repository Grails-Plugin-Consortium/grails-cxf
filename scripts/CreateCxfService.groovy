import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('Init')
includeTargets << grailsScript('_GrailsCreateArtifacts')

USAGE = """
	Usage: grails create-cxf-service <service-name>

	Creates a new Cxf Service wired with 'jax-ws' service type

	Example: grails create-cxf-service com.yourapp.FooService
"""

target(main: "Creates a new Cxf Service for the Cxf Plugin.") {
    depends(checkVersion, parseArguments)

    def type = "Service"
    promptForName(type: type)
    def name = purgeRedundantArtifactSuffix(argsMap["params"][0], type)

    createArtifact(name: name, suffix: type, type: "Endpoint", path: "grails-app/services")
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
