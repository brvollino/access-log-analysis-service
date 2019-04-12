pipeline {
    agent any
    parameters {
        booleanParam(name: 'BAKE_AMI', defaultValue: false, description: 'Bake an AMI into EC2')
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
                checkout scm
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
                    returnStdout: true)
            }
            steps {
                sh 'printenv'
                sh 'packer build ami-packer-config.json'
            }
        }
    }
}