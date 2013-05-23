Secured Remote EJB Example
==========================


Preliminary
-----------

Export JBOSS_HOME environment variable of your EAP 6.1 installation.

    export JBOSS_HOME=/path/to/eap610/jboss-eap-6.1

And configure Maven profiles in your `~/.m2/settings.xml` by following
this section, [Configure Maven]
(https://github.com/jboss-jdf/jboss-as-quickstart#configure-maven-).


Run EAP standalone server
-------------------------

    git clone https://github.com/onagano-rh/ejb-remote-security.git
    cd ejb-remote-security/eap610
    bin/standalone.sh -Djboss.server.base.dir=./standalone

Then an EAP standalone server runs in the `eap610` directory, using
configurations in the `standalone` directory.


Build and deploy sample EJB
---------------------------

    cd ../ejb-remote/server-side
    mvn install
    mvn jboss-as:deploy

When you modify the EJB source code, execute `mvn clean` in prior to
`mvn install` for sure.


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
