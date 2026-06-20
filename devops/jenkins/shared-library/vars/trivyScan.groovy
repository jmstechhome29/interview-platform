// Shared library step: fail the build on HIGH/CRITICAL CVEs.
def call(String image) {
    sh "trivy image --exit-code 1 --severity HIGH,CRITICAL ${image}"
}
