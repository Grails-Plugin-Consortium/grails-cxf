import groovyjarjarcommonscli.Option
import org.apache.commons.cli.HelpFormatter
import org.codehaus.gant.GantState

includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsClasspath")

javaSourceDir = "src${File.separator}java"
defaultOutPackage = 'org.grails.cxf.soap'

cli = null
options = null

target(wsdlToJava: 'Quick way to generate wsdl to java from cxf plugin') {
    depends(checkVersion)
    createCli()
    def ops = doSplit(args)
    printMessage "Using args: ${ops}"

    if(ops?.size() == 1 && (ops[0] == '-h' || ops[0] == '--help')){
        cli.usage()
        return false
    }

    options = cli.parse(ops)

    if(options?.wsdl) {
        wsdl2Java()
    }
}

printMessage = { String message -> event('StatusUpdate', [message]) }
finished = {String message -> event('StatusFinal', [message])}
errorMessage = { String message -> event('StatusError', [message]) }

private doSplit(String string){
    string.split(/(\n|[ ]|=)/).collect{ it.trim() }.findResults  { it && it != '' ? it : null }
}

private wsdl2Java() {

    ant.logger.setMessageOutputLevel(GantState.NORMAL)

    ant.java(classname: 'org.apache.cxf.tools.wsdlto.WSDLToJava') {
        arg(value: '-verbose')
        arg(value: '-d')
        arg(value: javaSourceDir)
        arg(value: '-p')
        arg(value: options?.package ?: defaultOutPackage)
        if(options?.fe) {
            arg(value: '-fe')
            arg(value: options.fe)
        }
        if(options?.db) {
            arg(value: '-db')
            arg(value: options.db)
        }
        if(options?.wv) {
            arg(value: '-wv')
            arg(value: options.wv)
        }
        if(options?.sn) {
            arg(value: '-sn')
            arg(value: options.sn)
        }
        if(options?.b) {
            arg(value: '-b')
            arg(value: options.b)
        }
        if(options?.catalog) {
            arg(value: '-catalog')
            arg(value: options.catalog)
        }
        if(options?.d) {
            arg(value: '-d')
            arg(value: options.d)
        }
        if(options?.compile) {
            arg(value: '-compile')
        }
        if(options?.classdir) {
            arg(value: '-classdir')
            arg(value: options.classdir)
        }
        if(options?.client) {
            arg(value: '-client')
        }
        if(options?.server) {
            arg(value: '-server')
        }
        if(options?.impl) {
            arg(value: '-impl')
        }
        if(options?.all) {
            arg(value: '-all')
        }
        if(options?.ant) {
            arg(value: '-ant')
        }
        if(options?.autoNameResolution) {
            arg(value: '-autoNameResolution')
        }
        if(options?.exsh) {
            arg(value: '-exsh')
            arg(value: options.exsh)
        }
        if(options?.dns) {
            arg(value: '-dns')
            arg(value: options.dns)
        }
        if(options?.dex) {
            arg(value: '-dex')
            arg(value: options.dex)
        }
        if(options?.validate) {
            arg(value: '-validate')
        }
        if(options?.keep) {
            arg(value: '-keep')
        }
        if(options?.noAddressBinding) {
            arg(value: '-noAddressBinding')
        }
        if(options?.exceptionSlurper) {
            arg(value: '-exceptionSlurper')
        }
        if(options?.reserveClass) {
            arg(value: '-reserveClass')
            arg(value: options.reserveClass)
        }
        if(options?.allowElementReferences) {
            arg(value: '-allowElementReferences')
            arg(value: options.allowElementReferences)
        }
        if(options?.asyncMethods) {
            arg(value: '-asyncMethods')
            arg(value: options.asyncMethods)
        }
        if(options?.bareMethods) {
            arg(value: '-bareMethods')
            arg(value: options?.bareMethods)
        }
        if(options?.bareMethodsAll) {
            arg(value: '-bareMethods')
        }
        if(options?.mimeMethods) {
            arg(value: '-mimeMethods')
            arg(value: options.mimeMethods)
        }
        if(options?.faultSerialVersionUID) {
            arg(value: '-faultSerialVersionUID')
            arg(value: options.faultSerialVersionUID)
        }
        if(options?.mark) {
            arg(value: '-mark-generated')
        }

        arg(value: options.wsdl)
    }

    finished "Finished generating Java code for WSDL."
}

private createCli() {
    String usageText = '''
grails wsdl-to-java --wsdl=<path to wsdl>
    [--p=<package>] [--fe=frontend-name] [--db=databinding-name]
    [--wv=wsdl-version] [--sn=service-name] [--b=binding-name]
    [--catalog=catalog-file-name] [--d output-directory] [--compile]
    [--classdir=compile-class-dir] [--client] [--server]
    [--impl] [--all] [--ant] [--autoNameResolution] [--exsh=(true/false)]
    [--dns=(true/false)] [--dex=(true/false)] [--validate] [--keep] [--noAddressBinding]
    [--exceptionSuper] [--reserveClass=classname] [--allowElementReferences<=true>]
    [--asyncMethods=foo,bar,...] [--bareMethods=foo,bar,...] [--bareMethodsAll]
    [--mimeMethods=foo,bar,...] [--mark]

See http://cxf.apache.org/docs/wsdl-to-java.html for more details.
'''

    String usageHeaderText = '''
Script Options:'''

    String usageFooterText = '''
See http://cxf.apache.org/docs/wsdl-to-java.html for additional options.'''

    cli = new CliBuilder(
            usage: usageText,
            header: usageHeaderText,
            footer: usageFooterText)

    cli.formatter = new HelpFormatter()
    cli.width = 150
    cli.writer = new PrintWriter(new StringWriter() {

        @Override
        void flush() {
            super.flush()
            errorMessage(toString())
        }
    })

    cli.with {
        h longOpt: 'help', 'Prints this help message: See http://cxf.apache.org/docs/wsdl-to-java.html for more details usages of these args.'
        wsdl longOpt: 'wsdl', args: 1, required: true, 'The path to the wsdl to use.'
        p longOpt: 'package', args: 1, 'The package to put the generated Java objects in.'
        fe longOpt: 'fe', args: 1, required: false, 'Specifies the frontend. Default is JAXWS. Currently supports only JAXWS frontend and a "jaxws21" frontend to generate JAX-WS 2.1 compliant code. '
        db longOpt: 'db', args: 1, required: false, 'Specifies the databinding. Default is jaxb. Currently supports jaxb, xmlbeans, sdo (sdo-static and sdo-dynamic), and jibx.'
        wv longOpt: 'wv', args: 1, required: false, 'Specifies the wsdl version .Default is WSDL1.1. Currently suppports only WSDL1.1 version.'
        sn longOpt: 'sn', args: 1, required: false, 'The WSDL service name to use for the generated code. '
        b longOpt: 'b', args: 1, required: false, 'Specifies JAXWS or JAXB binding files or XMLBeans context files. Use multiple -b flags to specify multiple entries.'
        catalog longOpt: 'catalog', args: 1, required: false, 'Specify catalog file to map the imported wsdl/schema'
        d longOpt: 'd', args: 1, required: false, 'Specifies the directory into which the generated code files are written. '
        compile longOpt: 'compile', args: 0, required: false, 'Compiles generated Java files.'
        classdir longOpt: 'classdir', args: 1, required: false, 'Specifies the directory into which the compiled class files are written. '
        client longOpt: 'client', args: 0, required: false, 'Generates starting point code for a client mainline. '
        server longOpt: 'server', args: 0, required: false, 'Generates starting point code for a server mainline. '
        impl longOpt: 'impl', args: 0, required: false, 'Generates starting point code for an implementation object. '
        all longOpt: 'all', args: 0, required: false, 'Generates all starting point code: types, service proxy, service interface, server mainline, client mainline, implementation object, and an Ant build.xml file.'
        'ant' longOpt: 'ant', args: 0, required: false, 'Specify to generate an Ant build.xml script.'
        autoNameResolution longOpt: 'autoNameResolution', args: 0, required: false, 'Automatically resolve naming conflicts without requiring the use of binding customizations. '
        exsh longOpt: 'exsh', args: 1, required: false, 'Enables or disables processing of implicit SOAP headers (i.e. SOAP headers defined in the wsdl:binding but not wsdl:portType section.) Processing the SOAP headers requires the SOAP binding jars available on the classpath which was not the default in CXF 2.4.x and older. You may need to add a dependency to cxf-rt-binding-soap for this flag to work. Default is false.'
        dns longOpt: 'dns', args: 1, required: false, 'Enables or disables the loading of the default namespace package name mapping. Default is true and http://www.w3.org/2005/08/addressing=org.apache.cxf.ws.addressing namespace package mapping will be enabled. '
        dex longOpt: 'dex', args: 1, required: false, 'Enables or disables the loading of the default excludes namespace mapping. Default is true. '
        validate longOpt: 'validate', args: 0, required: false, 'Enables validating the WSDL before generating the code. '
        keep longOpt: 'keep', args: 0, required: false, 'Specifies that the code generator will not overwrite any preexisting files. You will be responsible for resolving any resulting compilation issues. '
        noAddressBinding longOpt: 'noAddressBinding', args: 0, required: false, 'For compatibility with CXF 2.0, this flag directs the code generator to generate the older CXF proprietary WS-Addressing types instead of the JAX-WS 2.1 compliant WS-Addressing types. '
        exceptionSlurper longOpt: 'exceptionSlurper', args: 0, required: false, 'Superclass for fault beans generated from wsdl:fault elements (defaults to java.lang.Exception) '
        reserveClass longOpt: 'reserveClass', args: 1, required: false, 'Used with -autoNameResolution, defines a class names for wsdl-to-java not to use when generating classes. Use this option multiple times for multiple classes.'
        allowElementReferences longOpt: 'allowElementReferences', args: 1, required: false, 'If true, disregards the rule given in section 2.3.1.2(v) of the JAX-WS 2.2 specification disallowing element references when using wrapper-style mapping. '
        asyncMethods longOpt: 'asyncMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, 'List of subsequently generated Java class methods to allow for client-side asynchronous calls, similar to enableAsyncMapping in a JAX-WS binding file. '
        bareMethods longOpt: 'bareMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, 'List of subsequently generated Java class methods to have wrapper style, similar to enableWrapperStyle in JAX-WS binding file. '
        bareMethodsAll longOpt: 'bareMethodsAll', args: 0, required: false, 'All generated Java class methods to have wrapper style, similar to enableWrapperStyle in JAX-WS binding file. '
        mimeMethods longOpt: 'mimeMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, 'List of subsequently generated Java class methods to enable mime:content mapping, similar to enableMIMEContent in JAX-WS binding file. '
        faultSerialVersionUID longOpt: 'faultSerialVersionUID', args: 1, required: false, 'How to generate suid of fault exceptions. Use NONE, TIMESTAMP, FQCN, or a specific number. Default is NONE. '
        mark longOpt: 'mark', args: 0, required: false, 'Adds the @Generated annotation to classes generated.'
    }
}

setDefaultTarget(wsdlToJava)
