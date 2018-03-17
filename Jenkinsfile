pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
                sh './gradlew clean build'
            }
        }
    }
    post {
        success {
            githubNotify status: "SUCCESS", credentialsId: "Jenkins_for_GitHub", account: "YuriyGorvitovskiy", repo: "sql_storage"
        }
        failure {
            githubNotify status: "FAILURE", credentialsId: "Jenkins_for_GitHub", account: "YuriyGorvitovskiy", repo: "sql_storage"
        }
    }
}
