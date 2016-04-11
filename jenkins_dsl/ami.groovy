// file: jenkins_dsl/ami.groovy
//
// Amazon Linux AMI ID  http://aws.amazon.com/amazon-linux-ami/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
// Ubuntu 15.10 wily - https://cloud-images.ubuntu.com/locator/ec2/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
def ubuntu_ami = "ami-8b9087e1"
def amazon_ami = "ami-60b6c60a"
def git_repo = "https://github.com/kenzanlabs/ami-roles.git"

def amis = [  
              "ami-nexus":
                [
                  "name":"nexus",
                  "ami_profile":"nexus",
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
              "ami-base":
                [ 
                  "name":"base",
                  "ami_profile":"base",
                  "branch" : "master"
                ],
              "ami-karyon":
                [ 
                  "name":"karyon",
                  "ami_profile":"karyon",
                  "branch" : "master"
                ],
              "ami-haproxy":
                [
                  "name":"haproxy",
                  "ami_profile":"haproxy",
                  "branch" : "master"
                ],
              "ami-jenkins2.0":
                [
                  "name":"jenkins2.0",
                  "ami_profile":"jenkins2.0",
                  "branch" : "master"
                ],
              "ami-mongodb":
                [
                  "name":"mongodb",
                  "ami_profile":"mongodb",
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


