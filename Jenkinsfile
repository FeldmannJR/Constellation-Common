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
                withMaven(){
                    sh './mvnw clean deploy -P production'
                }
            }
        }
    }
}


