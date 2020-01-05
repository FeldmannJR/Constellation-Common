pipeline {
    podTemplate(containers: [
            containerTemplate(name: 'maven', image: 'maven:3.3.9-jdk-8-alpine', ttyEnabled: true, command: 'cat'),
    ])
    node() {
        container('maven') {
            stage('Deploy') {
                steps {
                    sh 'mvn clean deploy -P production'
                }
            }
        }
    }
}
