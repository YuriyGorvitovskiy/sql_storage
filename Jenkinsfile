pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Prepare') {
            githubNotify status: "PENDING", credentialsId: "Jenkins_for_GitHub"
        }
        stage('Build Server') {
            steps {
                sh './gradlew clean build'
            }
        }
    }
    post {
        success {
            githubNotify status: "SUCCESS", credentialsId: "Jenkins_for_GitHub"
        }
        failure {
            githubNotify status: "FAILURE", credentialsId: "Jenkins_for_GitHub"
        }
    }
}
