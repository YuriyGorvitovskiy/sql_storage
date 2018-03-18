pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
            		githubNotify status: "PENDING", credentialsId: "Jenkins_for_GitHub", description: "Build is in progress...", account: "YuriyGorvitovskiy", repo: "sql_storage"
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
