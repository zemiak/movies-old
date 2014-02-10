package com.zemiak.movies.lookup;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author vasko
 */
public class CDILookup {
    private static final Logger LOG = Logger.getLogger(CDILookup.class.getName());
    
    public <T> T lookup(final Class<T> type) {
        try {
            final BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
            final Set<Bean<?>> beans = beanManager.getBeans(type);
            final Bean<?> bean = beanManager.resolve(beans);
            final CreationalContext<?> cc = beanManager.createCreationalContext(bean);

            final T object = (T) beanManager.getReference(bean, type, cc);
            return object;
        } catch (final NamingException ex) {
            LOG.log(Level.SEVERE, "Can''t lookup object by {0}.{1}", new Object[]{type, ex.getMessage()});
            return null;
        }
    }
}