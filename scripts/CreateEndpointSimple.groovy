import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('Init')
includeTargets << grailsScript('_GrailsCreateArtifacts')

USAGE = """
	Usage: grails create-endpoint <endpoint-name>

	Creates a new Cxf Service Endpoint

	Example: grails create-endpoint com.yourapp.ApiEndpoint
"""

target(main: "Creates a new Cxf Service Endpoint for the Cxf Plugin.") {
    depends(checkVersion, parseArguments)

    def type = "Endpoint"
    promptForName(type: type)
    def name = purgeRedundantArtifactSuffix(argsMap["params"][0] + type, type)

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
