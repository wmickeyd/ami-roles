// file: jenkins_dsl/ami.groovy
//
// Amazon Linux AMI ID
def amiId = "ami-e7527ed7"


def amis = [  
              "ami-nexus": 
                [ "repo": ["https://github.com/kenzanmedia/ami-roles.git", "master"],
                  "name":"nexus",
                  "ami_profile":"nexus"
                ],              
              "ami-jenkins-slave": 
                [ "repo": ["https://github.com/kenzanmedia/ami-roles.git", "master"],
                  "name":"jenkins-slave",
                  "ami_profile":"jenkins-slave"
                ],
              "ami-tomcat7": 
                [ "repo": ["https://github.com/kenzanmedia/ami-roles.git", "master"],
                  "name":"tomcat7",
                  "ami_profile":"tomcat7"
                ],                
              "ami-jetty8": 
                [ "repo": ["https://github.com/kenzanmedia/ami-roles.git", "master"],
                  "name":"jetty8",
                  "ami_profile":"jetty8"
                ]
            ]


amis.values().each { ami ->
def jobname = "ami-" + ami.name
  
  freeStyleJob(jobname) {

    steps {
      shell('bin/provision_base_ami')
    }
      
    scm {
      git {
        remote {
          url(ami.repo.get(0))
          branch(ami.repo.get(1))
          credentials("GitHub")
        }
      }
    }

    publishers {
      archiveArtifacts { 
        pattern('AMI-$AMI_PROFILE')
      }
    }

    parameters {
      stringParam("AMI_ID", amiId,"AMI_ID - [HVM ID](http://aws.amazon.com/amazon-linux-ami/)")
      stringParam("AMI_PROFILE", ami.ami_profile, "Profile to use")
    }
  }
}


