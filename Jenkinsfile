pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Build Server') {
            steps {
            		echo 'Hello Jenkins'
                sh './gradlew clean build'
            }
        }
    }
}
