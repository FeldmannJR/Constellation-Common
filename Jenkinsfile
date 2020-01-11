pipeline {
    agent {
        kubernetes {
            //cloud 'kubernetes'
            defaultContainer 'maven'
            inheritFrom 'mavenTemplate'
        }
    }
    stages {
        stage('Deploy') {
            steps {
                sh 'mvn clean deploy -P production'
            }
        }
    }
}

