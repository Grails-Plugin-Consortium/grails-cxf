@artifact.package@

import org.grails.cxf.utils.EndpointType

class @artifact.name@ {
    static expose = EndpointType.JAX_WS
    static excludes = []

    String serviceMethod(String s) {
        return s
    }
}
