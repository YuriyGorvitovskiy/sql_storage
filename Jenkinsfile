pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {
    		stage('Prepare') {
	    		curl -XPOST -H "Authorization: token OAUTH TOKEN" https://api.github.com/repos/YuriyGorvitovskiy/sql_storage/statuses/$(git rev-parse HEAD) -d "{
					\"state\": \"pending\",
  					\"target_url\": \"${BUILD_URL}\",
  					\"description\": \"The build has pending!\"
			}"
    		}
        stage('Build Server') {
            steps {
                sh './gradlew clean build'
            }
        }
    }
    post {
        success {
        		curl -XPOST -H "Authorization: token OAUTH TOKEN" https://api.github.com/repos/YuriyGorvitovskiy/sql_storage/statuses/$(git rev-parse HEAD) -d "{
					\"state\": \"success\",
  					\"target_url\": \"${BUILD_URL}\",
  					\"description\": \"The build has succeeded!\"
			}"
        }
        failure {
        		curl -XPOST -H "Authorization: token OAUTH TOKEN" https://api.github.com/repos/YuriyGorvitovskiy/sql_storage/statuses/$(git rev-parse HEAD) -d "{
					\"state\": \"failure\",
  					\"target_url\": \"${BUILD_URL}\",
  					\"description\": \"The build has failed!\"
			}"
        }
    }
}
