package org.jboss.as.quickstarts.ejb.remote.secure;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.jboss.ejb3.annotation.SecurityDomain;

@Stateless
@Remote(SecureCalculator.class)
@RolesAllowed({"guest"})
@SecurityDomain("other")
public class SecureCalculatorBean implements SecureCalculator {

    @Override
    public int add(int a, int b) {
        System.out.println(this.getClass() + "#add called.");
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println(this.getClass() + "#subtract called.");
        return a - b;
    }
}
