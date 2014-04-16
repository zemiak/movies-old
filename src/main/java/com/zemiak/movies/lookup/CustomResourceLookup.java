package com.zemiak.movies.lookup;

import static javax.naming.InitialContext.doLookup;
import javax.naming.NamingException;

/**
 *
 * @author vasko
 */
public class CustomResourceLookup {
    public <T> T lookup(final String jndiName) {
        try {
            return doLookup(jndiName);
        } catch (NamingException e) {
            return null;
        }
    }
}
