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
            withMaven(){
                steps {
                    sh './mvnw clean deploy -P production'
                }
            }
        }
    }
}

