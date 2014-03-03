package com.zemiak.movies.admin.beans;

import com.zemiak.movies.admin.jsf.util.BeanValidator;
import com.zemiak.movies.strings.Joiner;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author vasko
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        validate(entity);
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        validate(entity);
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        return getEntityManager().createNamedQuery(entityClass.getSimpleName() + ".findAll").getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    private void validate(final T entity) {
        List<String> errors = new BeanValidator<T>().validate(entity);
        if (null != errors) {
            throw new IllegalStateException(Joiner.join(errors, ", "));
        }
    }
}
