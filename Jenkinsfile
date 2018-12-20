node {
   stage('Preparation') {
      git credentialsId: '9191002b-9e68-4db1-b4d9-74d2d06ae558', url: 'https://csanghub01plv.arcadsoftware.fr/sjulliand/ARCAD-Evolution.git'
   }
   stage('Build') {
       withMaven(jdk: 'Java 1.8', maven: 'Maven 3.5.4', mavenSettingsConfig: '11671e07-ac0b-4c13-8a30-53e730575d33') {
           if (isUnix()) {
               sh "mvn clean deploy -Ptycho"
           } else {
               bat(/mvn clean deploy -Ptycho/)
           }
       }
   }
}