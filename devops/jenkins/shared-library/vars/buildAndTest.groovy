// Shared library step: mvn build + test for a given module.
def call(String module) {
    sh "mvn -pl ${module} -am clean verify"
}
