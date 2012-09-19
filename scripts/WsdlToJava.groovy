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
    ant.java(fork: true, classpathref: "classpath", classname: 'org.apache.cxf.tools.wsdlto.WSDLToJava') {
        arg(value: '-verbose')
        arg(value: '-d')
        arg(value: javaSourceDir)
        arg(value: '-p')
        arg(value: options?.p ?: defaultOutPackage)
        arg(value: options.wsdl)
    }
    finished "Finished generating Java code for WSDL."
}

private createCli() {
    String usageText = '''grails wsdl-to-java --wsdl=<path to wsdl> [--package=<package>]'''

    String usageHeaderText = '''
Script Options:'''

    String usageFooterText = '''
See http://cxf.apache.org/docs/wsdl-to-java.html for additional options.'''

    cli = new CliBuilder(
            usage: usageText,
            header: usageHeaderText,
            footer: usageFooterText)

    cli.formatter = new HelpFormatter()
    cli.width = 120
    cli.writer = new PrintWriter(new StringWriter() {

        @Override
        void flush() {
            super.flush()
            errorMessage(toString())
        }
    })

    cli.h longOpt: 'help', 'Prints this help message'
    cli.w longOpt: 'wsdl', args: 1, required: true, 'The path to the wsdl to use.'
    cli.p longOpt: 'package', args: 1, 'The package to put the generated Java objects in.'
}

setDefaultTarget(main)
