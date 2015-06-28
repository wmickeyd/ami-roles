{# file: roles/oraclejdk8/templates/etc/profile.d/java.sh #}
export JRE_HOME={{ java_home }}/jre
export PATH=$PATH:$JRE_HOME/bin

export JAVA_ROOT={{ java_root }}
export JAVA_HOME={{ java_home }}
export JAVA_DEFAULT={{ java_default }}
export JAVA_PATH=$JAVA_HOME

export PATH=$PATH:$JAVA_HOME/bin