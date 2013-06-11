package org.jboss.as.quickstarts.ejb.remote.client;

import org.jboss.as.quickstarts.ejb.remote.secure.SecureCalculator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

public class RemoteEJBClient {

    public static void main(String[] args) throws Exception {
        // Suppress Remoting logs.
        Logger.getLogger("").setLevel(Level.OFF);

        final RemoteEJBClient client = new RemoteEJBClient();
        client.setEJBClientContext();

        System.out.println("Invoking in " + Thread.currentThread().getName());
        client.invokeStatelessBean();

        Thread t1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Invoking in " + Thread.currentThread().getName());
                        client.invokeStatelessBean();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        t1.start();
        t1.join();
    }

    private void invokeStatelessBean() throws NamingException {
        final Properties jndiProperties = new Properties();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        try {
            final SecureCalculator statelessRemoteCalculator = (SecureCalculator) context.lookup(
                "ejb:/jboss-as-ejb-remote-server-side/SecureCalculatorBean!" + SecureCalculator.class.getName()
            );

            // invoke on the remote calculator
            int a = 204;
            int b = 340;
            int sum = statelessRemoteCalculator.add(a, b);
            System.out.println("Remote calculator returned sum = " + sum);
            if (sum != a + b) {
                throw new RuntimeException("Remote stateless calculator returned an incorrect sum " + sum + " ,expected sum was " + (a + b));
            }
        } finally {
            context.close();
        }
    }

    private void setEJBClientContext() throws Exception {
        String hostName = "localhost";
        String port = "4447";
        String userName = "quickstartUser";
        String password = "quickstartPwd1!";
        Properties invokeProperties = new Properties();
        invokeProperties.put("endpoint.name", "client-endpoint");
        invokeProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        invokeProperties.put("remote.connections", "default");
        invokeProperties.put("remote.connection.default.host", hostName);
        invokeProperties.put("remote.connection.default.port", port);
        invokeProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
        invokeProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        invokeProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        invokeProperties.put("remote.connection.default.username", userName);
        invokeProperties.put("remote.connection.default.password", password);
        EJBClientConfiguration ejbcc = new PropertiesBasedEJBClientConfiguration(invokeProperties);
        ContextSelector<EJBClientContext> ejbCtxSel = new ConfigBasedEJBClientContextSelector(ejbcc);
        EJBClientContext.setSelector(ejbCtxSel);
    }
}
