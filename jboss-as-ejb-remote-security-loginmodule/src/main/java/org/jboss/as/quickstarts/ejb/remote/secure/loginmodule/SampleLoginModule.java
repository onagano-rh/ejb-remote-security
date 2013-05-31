package org.jboss.as.quickstarts.ejb.remote.secure.loginmodule;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

public class SampleLoginModule extends UsernamePasswordLoginModule
{
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map sharedState;
    private Map options;
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
    {
        System.out.println("initialize");
        System.out.println("CallbackHandler impl is " + callbackHandler.getClass());
        super.initialize(subject, callbackHandler, sharedState, options);
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }
    
    protected Group[] getRoleSets() throws LoginException
    {
        String[] roles = new String[] {"guest"};
        Group[] groups = {new SimpleGroup("Roles")};
        System.out.println("Getting roles for user=" + super.getUsername());
        for(int r = 0; r < roles.length; r++) {
            SimplePrincipal role = new SimplePrincipal(roles[r]);
            System.out.println("Found role=" + roles[r]);
            groups[0].addMember(role);
        }
        return groups;
    }
                    
    protected String getUsersPassword() throws LoginException
    {
        String[] info = getUsernameAndPassword();
        System.out.println("Supplied username=" + info[0]);
        System.out.println("Supplied password=" + info[1]);
        System.out.println("Option hashUserPassword=" + options.get("hashUserPassword"));
        System.out.println("Option hashStorePassword=" + options.get("hashStorePassword"));
        System.out.println("Option inputValidator=" + options.get("inputValidator"));

        Object cred = getCredentials();
        if (cred != null) {
            if (cred instanceof char[])
                System.out.println("Supplied credential=" + new String((char[]) cred) + " " + cred.getClass());
            else
                System.out.println("Supplied credential=" + cred.toString() + " " + cred.getClass());
        }

        System.out.println("Getting password for user=" + super.getUsername());
        String passwd = "quickstartPwd1!";
        System.out.println("Found password=" + passwd);
        return passwd;
    }
}
