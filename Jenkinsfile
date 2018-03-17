pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
            		echo 'Hello Jenkins & Github!'
                sh './gradlew clean build'
            }
        }
    }
}
