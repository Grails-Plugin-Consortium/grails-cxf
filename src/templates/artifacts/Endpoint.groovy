@artifact.package@
class @artifact.name@ {
    static exposeAs = 'cxf'
    static excludes = []

    String serviceMethod(String s) {
        return s
    }
}