@artifact.package@

import org.grails.cxf.utils.EndpointType

class @artifact.name@ {
    static expose = EndpointType.SIMPLE
    static excludes = []

    String serviceMethod(String s) {
        return s
    }
}
