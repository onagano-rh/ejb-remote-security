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
    
    /**
     * Override to obtain the userPathPrefix and rolesPathPrefix options.
     */
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map sharedState, Map options)
    {
        System.out.println("initialize");
        try {
            Map newOptions = new HashMap(options);
        super.initialize(subject, callbackHandler, sharedState, newOptions);
        } catch (Throwable e ){
            e.printStackTrace();
        }
    }
    
    /**
     *  Get the roles the current user belongs to by querying the
     * rolesPathPrefix + '/' + super.getUsername() JNDI location.
     */
    protected Group[] getRoleSets() throws LoginException
    {
        String[] roles = new String[] { "all_users" };
        Group[] groups = {new SimpleGroup("Roles")};
        System.out.println("Getting roles for user="+super.getUsername());
        for(int r = 0; r < roles.length; r ++) {
            SimplePrincipal role = new SimplePrincipal(roles[r]);
            System.out.println("Found role="+roles[r]);
            groups[0].addMember(role);
        }
        return groups;
    }
                    
    /** 
     * Get the password of the current user by querying the
     * userPathPrefix + '/' + super.getUsername() JNDI location.
     */
    protected String getUsersPassword() 
        throws LoginException
    {
        System.out.println("Getting password for user="+super.getUsername());
        String passwd = "admin";
        System.out.println("Found password="+passwd);
        return passwd;
        
    }
}
