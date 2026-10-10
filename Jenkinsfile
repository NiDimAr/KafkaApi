pipeline {
    agent any
    tools { maven 'Maven3' }

    environment {
        TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
    }

    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

    post {
        always {
            allure results: [[path: 'target/allure-results']]
        }
    }
}