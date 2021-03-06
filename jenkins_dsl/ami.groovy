// file: jenkins_dsl/ami.groovy
//
// Amazon Linux AMI ID  http://aws.amazon.com/amazon-linux-ami/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
// Ubuntu 14.04 LTS  - https://cloud-images.ubuntu.com/locator/ec2/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
// Ubuntu 14.04LTS - https://cloud-images.ubuntu.com/locator/ec2/
def ubuntu_ami = "ami-fce3c696"
def amazon_ami = "ami-60b6c60a"
def git_repo = "https://github.com/kenzanlabs/ami-roles.git"

def amis = [  
              "ami-nexus":
                [
                  "name":"nexus",
                  "ami_profile":"nexus",
                  "branch" : "master"
                ],
              "ami-nexus3":
                [
                  "name":"nexus3",
                  "ami_profile":"nexus3",
                  "branch" : "master"
                ],
              "ami-tomcat7":
                [ 
                  "name":"tomcat7",
                  "ami_profile":"tomcat7",
                  "branch" : "master"
                ],
              "ami-tomcat8":
                [ 
                  "name":"tomcat8",
                  "ami_profile":"tomcat8",
                  "branch" : "master"
                ],
              "ami-jetty8":
                [ 
                  "name":"jetty8",
                  "ami_profile":"jetty8",
                  "branch" : "master"
                ],
              "ami-jetty9":
                [
                  "name":"jetty9",
                  "ami_profile":"jetty9",
                  "branch" : "master"
                ],
              "ami-base":
                [ 
                  "name":"base",
                  "ami_profile":"base",
                  "branch" : "master"
                ],
              "ami-jservice":
                [ 
                  "name":"jservice",
                  "ami_profile":"jservice",
                  "branch" : "master"
                ],
              "ami-haproxy":
                [
                  "name":"haproxy",
                  "ami_profile":"haproxy",
                  "branch" : "master"
                ],
               "ami-edda":
                [
                  "name":"edda",
                  "ami_profile":"edda",
                  "branch" : "master"
		            ],
              "ami-jenkins2.0":
                [
                  "name":"jenkins2.0",
                  "ami_profile":"jenkins2.0",
                  "branch" : "master"
                ],		
              "ami-simianarmy":
                [
                  "name":"simianarmy",
                  "ami_profile":"simianarmy",
                  "branch": "master"
                ],
              "ami-mongodb":
                [
                  "name":"mongodb",
                  "ami_profile":"mongodb",
                  "branch" : "master"
                ],
               "ami-jenkins-slave":
                [
                  "name":"jenkins-slave",
                  "ami_profile":"jenkins-slave",
                  "branch" : "master"
                ]
            ]


amis.values().each { ami ->
def jobname = "ami-" + ami.name
  
  freeStyleJob(jobname) {

    steps {
      shell('$WORKSPACE/bin/provision_base_ami')
    }
      
    scm {
      git {
        remote {
          url(git_repo)
          branch(ami.branch)
          credentials("GitHub")
        }
      }
    }

    wrappers {
      preBuildCleanup()
    }

    publishers {
      archiveArtifacts { 
        pattern('AMI-$AMI_PROFILE')
      }
    }
    parameters {
      stringParam("UBUNTUAMI", ubuntu_ami, "Ubuntu amiid to be used")
      stringParam("AMAZONAMI", amazon_ami, "Amazon amiid to be used")
      stringParam("AMI_PROFILE", ami.ami_profile, "Profile to use")
    }
    
    parameters {
      choiceParam("OS", ['Both', 'Ubuntu','Amazon'], 'OS - Choices are Ubuntu or Amazon or build one of each')
    }
    
  }
}


