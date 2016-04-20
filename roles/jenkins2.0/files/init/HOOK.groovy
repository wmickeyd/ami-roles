import jenkins.model.*
import hudson.security.*
import java.util.logging.Logger

def logger = Logger.getLogger("")
def installed = false
def initialized = false
// add plugins
def pluginParameter="chucknorris git github-api github git-parameter job-dsl parameterized-trigger shelve-project-plugin ssh swarm credentials-binding workflow-step-api ws-cleanup plain-credentials postbuildscript"
def plugins = pluginParameter.split()
logger.info("" + plugins)
def instance = Jenkins.getInstance()
def pm = instance.getPluginManager()
def uc = instance.getUpdateCenter()
uc.updateAllSites()

plugins.each {
 logger.info("Checking " + it)
 if (!pm.getPlugin(it)) {
   logger.info("Looking UpdateCenter for " + it)
   if (!initialized) {
     uc.updateAllSites()
     initialized = true
   }
   def plugin = uc.getPlugin(it)
   if (plugin) {
     logger.info("Installing " + it)
       plugin.deploy()
     installed = true
   }
 }
}
// remove new installation configuration wizard and null out admin password
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "")
instance.setSecurityRealm(hudsonRealm)
instance.save()
if (installed) {
 logger.info("Plugins installed, initializing a restart!")
 instance.save()
 instance.doSafeRestart()
}
