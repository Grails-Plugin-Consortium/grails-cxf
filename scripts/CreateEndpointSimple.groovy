import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('Init')
includeTargets << grailsScript('_GrailsCreateArtifacts')

USAGE = """
	Usage: grails create-endpoint-simple <endpoint-name>

	Creates a new Cxf Endpoint wired with 'simple' service type

	Example: grails create-endpoint-simple com.yourapp.SimpleEndpoint
"""

target(main: "Creates a new Cxf Endpoint for the Cxf Plugin.") {
    depends(checkVersion, parseArguments)

    def type = "Endpoint"
    promptForName(type: type)
    def name = purgeRedundantArtifactSuffix(argsMap["params"][0], type)

    createArtifact(name: name, suffix: type, type: "${type}Simple", path: "grails-app/endpoints")
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
