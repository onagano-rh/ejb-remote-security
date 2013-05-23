package org.jboss.as.quickstarts.ejb.remote.secure;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(SecureCalculator.class)
public class SecureCalculatorBean implements SecureCalculator {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }
}
