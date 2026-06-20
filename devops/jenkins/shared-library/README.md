# interview-platform-shared-lib

Jenkins shared library used by every service's Jenkinsfile so pipeline logic
(build, docker push, trivy scan) lives in one place instead of being copy-pasted
into 5 Jenkinsfiles.

Register in Jenkins: Manage Jenkins -> System -> Global Pipeline Libraries
  Name: interview-platform-shared-lib
  Source: this `devops/jenkins/shared-library` folder (as its own git repo)

Usage in a Jenkinsfile:

    @Library('interview-platform-shared-lib') _
    buildAndTest('user-service')
    dockerBuildPush(image: '...', tag: '1', dockerfile: 'user-service/Dockerfile')
    trivyScan('123456789012.dkr.ecr.us-east-1.amazonaws.com/interview-platform/user-service:1')
