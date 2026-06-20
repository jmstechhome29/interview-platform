// Job DSL seed job: creates one pipeline job per microservice so you don't
// hand-create 5 jobs in the Jenkins UI. Run this once from a "seed" freestyle
// job that has the Job DSL plugin step pointed at this file.
def services = ['user-service', 'resume-service', 'interview-service', 'feedback-service', 'api-gateway']

services.each { svc ->
    pipelineJob("interview-platform/${svc}") {
        definition {
            cpsScm {
                scm {
                    git {
                        remote { url('https://github.com/your-org/interview-platform.git') }
                        branch('main')
                    }
                }
                scriptPath('devops/jenkins/Jenkinsfile')
            }
        }
        parameters {
            stringParam('SERVICE_NAME', svc, 'Microservice to build')
        }
        triggers {
            githubPush()
        }
    }
}
