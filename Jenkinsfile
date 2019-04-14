pipeline {
    agent any
    parameters {
        booleanParam(name: 'BAKE_AMI', defaultValue: false, description: 'Bake an AMI into EC2')
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Bake AMI') {
            when {
                expression { params.BAKE_AMI }
            }
            environment {
                AWS_ACCESS_KEY = credentials('aws-access-credential')
                PROJECT_VERSION = sh(
                    script: './gradlew properties | grep -Po "(?<=^version: ).*"',
                    returnStdout: true).trim()
                CURRENT_DIR = sh(
                        script: 'ls -lah ./build/distributions',
                        returnStdout: true).trim()
            }
            steps {
                sh 'packer build ubuntu-with-java-packer-config.json'
                sh 'packer build ami-packer-config.json'
            }
        }
    }
}