node {
	stage('Preparation') {
		checkout scm
	}
	stage('Build') {
		withMaven(jdk: 'Java 1.8', maven: 'Maven 3.5.4', mavenSettingsConfig: '11671e07-ac0b-4c13-8a30-53e730575d33') {
			if (isUnix()) {
				sh "mvn clean deploy -Ptycho"
			} else {
				bat "mvn clean deploy -Ptycho"
			}
		}
	}
}
