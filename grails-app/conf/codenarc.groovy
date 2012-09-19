ruleset {

    description 'CodeNarc RuleSet'

//    ruleset( "http://codenarc.sourceforge.net/StarterRuleSet-AllRulesByCategory.groovy.txt" ) {
    ruleset("file:grails-app/conf/codenarc.ruleset.all.groovy.txt") {
        DuplicateNumberLiteral   ( enabled : false )
        DuplicateStringLiteral   ( enabled : false )
        BracesForClass           ( enabled : false )
        BracesForMethod          ( enabled : false )
        BracesForIfElse          ( enabled : false )
        BracesForForLoop         ( enabled : false )
        BracesForTryCatchFinally ( enabled : false )
        JavaIoPackageAccess      ( enabled : false )
        ThrowRuntimeException    ( enabled : false )
        CatchException           ( enabled : false )

        AbcComplexity            ( maxMethodComplexity : 70  )
        LineLength               ( length              : 220 )
        MethodName               ( regex               : /[a-z][\w\s'\(\)]*/ ) // Spock method names
    }
}