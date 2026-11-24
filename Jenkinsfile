pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Tests') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                    jacoco execPattern: 'target/jacoco.exec',
                           classPattern: 'target/classes',
                           sourcePattern: 'src/main/java'
                }
            }
        }
        stage('Package') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/gestion-location-vehicules-1.0.0.jar', fingerprint: true
            }
        }
        stage('Run (simulation de déploiement)') {
            options {
                timeout(time: 2, unit: 'MINUTES')
            }
            steps {
                bat 'java -jar target\\gestion-location-vehicules-1.0.0.jar --spring.main.web-application-type=none'
            }
        }
    }

    post {
        success { echo 'Pipeline réussi' }
        failure { echo 'Pipeline en échec' }
    }
}