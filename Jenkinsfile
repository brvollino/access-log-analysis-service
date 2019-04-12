node {
    stage('Checkout code') {
        checkout scm
    }

    stage('Build') {
        sh './gradlew build'
    }
}