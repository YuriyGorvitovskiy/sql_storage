pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
            		githubNotify status: "PENDING", credentialsId: "YuriyGorvitovskiy@GitHub", description: "Build is in progress...", account: "YuriyGorvitovskiy", repo: "sql_storage"
                sh './gradlew clean build'
            }
        }
    }
    post {
        success {
        		githubNotify status: "SUCCESS", credentialsId: "YuriyGorvitovskiy@GitHub", description: "Build succeeded!", account: "YuriyGorvitovskiy", repo: "sql_storage"
        }
        failure {
        		githubNotify status: "FAILURE", credentialsId: "YuriyGorvitovskiy@GitHub", description: "Build failed!", account: "YuriyGorvitovskiy", repo: "sql_storage"
        }
    }
}
