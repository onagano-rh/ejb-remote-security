Secured Remote EJB Example
==========================


Preliminary
-----------

Export JBOSS_HOME environment variable of your EAP 6.1 installation.

    export JBOSS_HOME=/path/to/jboss-eap-6.1

And download
[EAP 6.1 Maven Repository]
(https://access.redhat.com/jbossnetwork/restricted/softwareDetail.html?softwareId=21743&product=appplatform&version=6.1.0&downloadType=distributions).
Assume it is extracted into `/path/to` directory.

Then confiure Maven profiles in your `~/.m2/settings.xml` which must look
like, or contain the profiles of, `example-settings.xml` of this repository.
Note that the path `file:///path/to/jboss-eap-6.1.0.GA-maven-repository`
should be matched with your actual EAP 6.1 Maven Repository installation.
You can find more details about this configuration at
[Configure Maven]
(https://github.com/jboss-jdf/jboss-as-quickstart#configure-maven-),
if needed.


Run EAP standalone server
-------------------------

    git clone https://github.com/onagano-rh/ejb-remote-security.git
    cd ejb-remote-security
    git checkout -b custom-security origin/custom-security
    cd eap610
    bin/standalone.sh -Djboss.server.base.dir=./standalone &

Then an EAP standalone server runs in the `eap610` directory, using
configurations in the `standalone` directory.


Build sample login module and configure it
------------------------------------------

    cd ../jboss-as-ejb-remote-security-loginmodule
    mvn package
    $JBOSS_HOME/bin/jboss-cli.sh -c --user=quickstartUser --password=quickstartPwd1!
    # In JBoss CLI
    module add --name=org.jboss.as.quickstarts.ejb.remote.secure.loginmodule --resources=/path/to/ejb-remote-security/jboss-as-ejb-remote-security-loginmodule/target/jboss-as-ejb-remote-security-loginmodule-7.1.2-SNAPSHOT.jar --dependencies=javax.api,org.picketbox

Sample login module is already configured in standalone.xml but this assumes
the module is installed properly.  The above CLI command installs the module.
This step is required only at the first time.


Build and deploy sample EJB
---------------------------

    cd ../ejb-remote/server-side
    mvn install
    mvn jboss-as:deploy

When you modify the EJB source code, execute `mvn clean` in prior to
`mvn install` for sure.
If you are asked for username and password when deploying, use "quickstartUser" and "quickstartPwd1!".


Build and run sample client
---------------------------

    cd ../client
    mvn compile
    mvn exec:exec

When you modify the client source code execute `mvn clean` in priof to
`mvn compile` for sure.

`mvn exec:exec` will find `./target/classes/jboss-ejb-client.properties`
for the connecting informantion like username and password.
Edit this file, not the one in `./src/main/resources` for a temporary change.


How to add users
----------------

    $JBOSS_HOME/bin/add-user.sh -sc ./standalone/configuration -dc . -a -u quickstartUser -p quickstartPwd1! --role guest -r ApplicationRealm
    $JBOSS_HOME/bin/add-user.sh -sc ./standalone/configuration -dc . -a -u user1 -p password1! --role app-user -r ApplicationRealm

Users `quickstartUser` and `user1` have already been added into
`application-users.properties` and `application-roles.properties`
files in `eap610/standalone/configuration` directory by the above
command.  The command assumes you are in `eap610` directory of this
repository.
Find their password, role, and realm in the command line.

If you want to add a management user, use the command like this.

    $JBOSS_HOME/bin/add-user.sh -sc ./standalone/configuration -dc . -u admin -p RedHat1!
