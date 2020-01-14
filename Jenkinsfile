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
                    sh 'export PATH=$MVN_CMD_DIR:$PATH && mvn clean deploy -P production'
                }
            }
        }
    }
}


