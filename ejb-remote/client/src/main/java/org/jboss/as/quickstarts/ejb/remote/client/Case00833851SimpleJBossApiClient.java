/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.as.quickstarts.ejb.remote.client;

// import com.digiquant.ims.ejb.account.business.AccountAgent;
// import com.digiquant.ims.ejb.account.valueobject.Account;
import org.jboss.as.quickstarts.ejb.remote.secure.SecureCalculator;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

public class Case00833851SimpleJBossApiClient {

    private static final Logger LOGGER = Logger.getLogger(Case00833851SimpleJBossApiClient.class.getName());
    final String user;
    final String password;
    final String port;

    public Case00833851SimpleJBossApiClient(String user, String password, String port, Boolean debug) {
        this.user = user;
        this.password = password;
        this.port = port;

        Level l = debug == null ? Level.SEVERE : debug.booleanValue() ? Level.ALL : Level.INFO;
        Logger.getLogger("").setLevel(l);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.ALL);
        Logger.getLogger(Case00833851SimpleJBossApiClient.class.getPackage().getName()).setLevel(
            debug != null ? Level.FINEST : Level.INFO);
    }

    /**
     * Invoke using Remote Naming.
     */
    private void invokeUsingRemoteNaming() throws NamingException {
        Properties p = new Properties();
        // p.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        p.put("jboss.naming.client.ejb.context","true");
        p.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        p.put(Context.PROVIDER_URL, "remote://localhost:" + this.port);
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        p.put(Context.SECURITY_PRINCIPAL, this.user);
        p.put(Context.SECURITY_CREDENTIALS, this.password);
        LOGGER.info("PARAMS : " + p);

        Properties props = new Properties();
        // props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.putAll(p);
        InitialContext context = new InitialContext(props);

        try {
             // final String rcal = "IMSBusinessComponents/ejbs/AccountAgentBean!" + AccountAgent.class.getName();
             // final AccountAgent remote = (AccountAgent) context.lookup(rcal);
             // final Account result = remote.findAccountByKey(1L);

           final String rcal = "jboss-as-ejb-remote-server-side/SecureCalculatorBean!" + SecureCalculator.class.getName();
           final SecureCalculator remote = (SecureCalculator) context.lookup(rcal);
           int a = 204;
           int b = 340;
           LOGGER.info("Calling EJB...");
           final int result = remote.add(a, b);
           LOGGER.info("The EJB call returns : " + result);
        } finally {
            context.close();
        }
    }

    /**
     * Invoke with EJB Client API.
     */
    private void invokeWithEJBClientAPI() throws NamingException {
        Properties p = new Properties();
        // p.put("endpoint.name", "nonClustered-client-endpoint");
        p.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        // p.put("deployment.node.selector", SimpleLoadFactorNodeSelector.class.getName());
        p.put("remote.connections", "appTwoA");
        p.put("remote.connection.appTwoA.port", this.port);
        p.put("remote.connection.appTwoA.host", "localhost");
        p.put("remote.connection.appTwoA.username", this.user);
        p.put("remote.connection.appTwoA.password", this.password);
        p.put("remote.connection.appTwoA.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        LOGGER.info("PARAMS : " + p);

        EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(p);
        ContextSelector<EJBClientContext> selector = new ConfigBasedEJBClientContextSelector(cc);
        EJBClientContext.setSelector(selector);

        Properties props = new Properties();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        //props.putAll(p);
        InitialContext context = new InitialContext(props);

        try {
             // final String rcal = "ejb:IMSBusinessComponents/ejbs/AccountAgentBean!" + AccountAgent.class.getName();
             // final AccountAgent remote = (AccountAgent) context.lookup(rcal);
             // final Account result = remote.findAccountByKey(1L);

           final String rcal = "ejb:/jboss-as-ejb-remote-server-side/SecureCalculatorBean!" + SecureCalculator.class.getName();
           final SecureCalculator remote = (SecureCalculator) context.lookup(rcal);
           int num1 = 3434;
           int num2 = 2332;
           LOGGER.info("Calling EJB...");
           final int result = remote.subtract(num1, num2);
           LOGGER.info("The EJB call returns : " + result);
        } finally {
            context.close();
        }
    }

    /**
     * Main method.
     */
    public static void main(String[] args) throws NamingException {
        String user = null, passwd = null;
        String port = null;
        Boolean debug = null;

        // user = "administrator";
        // passwd = "admin";
        user = "quickstartUser";
        passwd = "quickstartPwd1!";
        port = "4447";
        if (args.length > 0 && "-D".equals(args[0])) {
            debug = Boolean.TRUE;
        }
        // debug = Boolean.TRUE;

        Case00833851SimpleJBossApiClient client1 = new Case00833851SimpleJBossApiClient(user, passwd, port, debug);
        client1.invokeUsingRemoteNaming();
        client1.invokeUsingRemoteNaming();
        client1.invokeUsingRemoteNaming();
        client1.invokeUsingRemoteNaming();

        Case00833851SimpleJBossApiClient client2 = new Case00833851SimpleJBossApiClient(user, passwd, port, debug);
        client2.invokeWithEJBClientAPI();
        client2.invokeWithEJBClientAPI();
        client2.invokeWithEJBClientAPI();
        client2.invokeWithEJBClientAPI();
    }
}
