// Shared library step: build + push a docker image to a registry.
def call(Map config) {
    sh """
        docker build -t ${config.image}:${config.tag} -f ${config.dockerfile} ${config.context ?: '.'}
        docker push ${config.image}:${config.tag}
    """
}
