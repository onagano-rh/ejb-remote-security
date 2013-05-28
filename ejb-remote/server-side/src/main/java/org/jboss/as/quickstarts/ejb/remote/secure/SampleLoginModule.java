package org.jboss.as.quickstarts.ejb.remote.secure;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import org.jboss.security.NestableGroup;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

public class SampleLoginModule extends UsernamePasswordLoginModule
{
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
    {
        System.out.println("initialize");
        super.initialize(subject, callbackHandler, sharedState, options);
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
        System.out.println("Getting password for user=" + super.getUsername());
        String passwd = "quickstartPwd1!";
        System.out.println("Found password=" + passwd);
        return passwd;
    }
}
