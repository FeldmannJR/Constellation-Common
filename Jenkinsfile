pipeline {
    agent {
        kubernetes {
            //cloud 'kubernetes'
            defaultContainer 'maven'
            yaml """
                apiVersion: v1
                kind: Pod
                spec:
                    containers:
                        - name: maven
                          image: maven:3.3.9-jdk-8-alpine
                          command: ['cat']
                          tty: true
                          volumeMounts:
                            - mountPath: /root/.m2/repository
                              name: maven-cache-storage
                    volumes:
                        - name: maven-cache-storage
                          persistentVolumeClaim:
                            claimName: maven-cache-pv-claim
            """
        }
    }
    stages {
        stage('Deploy') {
            steps {
                container(name: 'maven') {
                    sh 'mvn clean deploy -P production'
                }
            }
        }
    }
}

