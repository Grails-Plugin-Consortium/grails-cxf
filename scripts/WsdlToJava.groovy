import groovyjarjarcommonscli.Option
import org.codehaus.groovy.tools.shell.util.HelpFormatter

includeTargets << grailsScript("Init")
includeTargets << grailsScript("_GrailsClasspath")

argsArray = [] as String[]

javaSourceDir = "src${File.separator}java"
defaultOutPackage = 'org.grails.cxf.soap'

cli = null
options = null

target(main: 'Quick way to generate wsdl to java from cxf plugin') {
    depends(checkVersion)

    createCli()
    options = cli.parse(args.tokenize())

    if(options.help) {
        cli.usage()
        return false
    }

    wsdl2Java()
}

printMessage = { String message -> event('StatusUpdate', [message]) }
finished = {String message -> event('StatusFinal', [message])}
errorMessage = { String message -> event('StatusError', [message]) }

private wsdl2Java() {

    ant.java(classname: 'org.apache.cxf.tools.wsdlto.WSDLToJava') {
        arg(value: '-verbose')
        arg(value: '-d')
        arg(value: javaSourceDir)
        arg(value: '-p')
        arg(value: options?.p ?: defaultOutPackage)
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
        if(options?.ca) {
            arg(value: '-catalog')
            arg(value: options.ca)
        }
        if(options?.d) {
            arg(value: '-d')
            arg(value: options.d)
        }
        if(options?.cmpl) {
            arg(value: '-compile')
        }
        if(options?.clsdir) {
            arg(value: '-classdir')
            arg(value: options.clsdir)
        }
        if(options?.clnt) {
            arg(value: '-client')
        }
        if(options?.srv) {
            arg(value: '-server')
        }
        if(options?.impl) {
            arg(value: '-impl')
        }
        if(options?.all) {
            arg(value: '-all')
        }
        if(options?.doant) {
            arg(value: '-ant')
        }
        if(options?.anr) {
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
        if(options?.nobind) {
            arg(value: '-noAddressBinding')
        }
        if(options?.exslrpr) {
            arg(value: '-exceptionSlurper')
        }
        if(options?.reserveClass) {
            arg(value: '-reserveClass')
            arg(value: options.reserveClass)
        }
        if(options?.allow) {
            arg(value: '-allowElementReferences')
            arg(value: options.allow)
        }
        if(options?.async) {
            arg(value: '-asyncMethods')
            arg(value: options.async)
        }
        if(options?.bare) {
            arg(value: '-bareMethods')
            arg(value: options.bare)
        }
        if(options?.mime) {
            arg(value: '-mimeMethods')
            arg(value: options.mime)
        }
        if(options?.fault) {
            arg(value: '-faultSerialVersionUID')
            arg(value: options.fault)
        }
        if(options?.mark) {
            arg(value: '-mark-generated')
        }

        arg(value: "-wsdlList")
        arg(value: options.wsdl)
    }

    finished "Finished generating Java code for WSDL."
}

private createCli() {
    String usageText = '''
grails wsdl-to-java --wsdl=<path to wsdl>
    [--package=<package>] [--fe=frontend-name] [--db=databinding-name]
    [--wv=wsdl--version] [--sn=service-name] [--b=binding-name]
    [--catalog=catalog-file-name] [--d output-directory] [--compile]
    [--classdir=compile-class-dir] [--client] [--server]
    [--impl] [--all] [--ant] [--autoNameResolution] [--exsh=(true/false)]
    [--dns=(true/false)] [--dex=(true/false)] [--validate] [--keep] [--noAddressBinding]
    [--exceptionSuper] [--reserveClass=classname] [--allowElementReferences<=true>]
    [--asyncMethods=foo,bar,...] [--bareMethods=foo,bar,...]
    [--mimeMethods=foo,bar,...] [--mark-generated]

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
    cli.width = 250
    cli.writer = new PrintWriter(new StringWriter() {

        @Override
        void flush() {
            super.flush()
            errorMessage(toString())
        }
    })

    cli.with {
        h longOpt: 'help', 'Prints this help message: See http://cxf.apache.org/docs/wsdl-to-java.html for more details usages of these args.'
        w longOpt: 'wsdl', args: 1, required: true, 'The path to the wsdl to use.'
        p longOpt: 'package', args: 1, 'The package to put the generated Java objects in.'
        fe longOpt: 'fe', args: 1, required: false, 'Specifies the frontend. Default is JAXWS. Currently supports only JAXWS frontend and a "jaxws21" frontend to generate JAX-WS 2.1 compliant code. '
        db longOpt: 'db', args: 1, required: false, 'Specifies the databinding. Default is jaxb. Currently supports jaxb, xmlbeans, sdo (sdo-static and sdo-dynamic), and jibx.'
        wv longOpt: 'wv', args: 1, required: false, ''
        sn longOpt: 'sn', args: 1, required: false, ''
        b longOpt: 'b', args: 1, required: false, 'binding name'
        ca longOpt: 'catalog', args: 1, required: false, ''
        d longOpt: 'd', args: 1, required: false, ''
        cmpl longOpt: 'compile', args: 0, required: false, ''
        clsdir longOpt: 'classdir', args: 1, required: false, ''
        clnt longOpt: 'client', args: 0, required: false, ''
        srv longOpt: 'server', args: 0, required: false, ''
        impl longOpt: 'impl', args: 0, required: false, ''
        all longOpt: 'all', args: 0, required: false, ''
        doant longOpt: 'ant', args: 0, required: false, ''
        anr longOpt: 'autoNameResolution', args: 0, required: false, ''
        exsh longOpt: 'exsh', args: 1, required: false, ''
        dns longOpt: 'dns', args: 1, required: false, ''
        dex longOpt: 'dex', args: 1, required: false, ''
        validate longOpt: 'validate', args: 0, required: false, ''
        keep longOpt: 'keep', args: 0, required: false, ''
        nobind longOpt: 'noAddressBinding', args: 0, required: false, ''
        exslrpr longOpt: 'exceptionSlurper', args: 0, required: false, ''
        reserveClass longOpt: 'reserveClass', args: 1, required: false, ''
        allow longOpt: 'allowElementReferences', args: 1, required: false, ''
        async longOpt: 'asyncMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, ''
        bare longOpt: 'bareMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, ''
        mime longOpt: 'mimeMethods', args: Option.UNLIMITED_VALUES, valueSeparator: ',' as char, required: false, ''
        fault longOpt: 'faultSerialVersionUID', args: 1, required: false, ''
        mark longOpt: 'mark-generated', args: 0, required: false, ''
    }
}

setDefaultTarget(main)
