package com.demo.dao;

import com.demo.model.AbstractModel;
import com.demo.utils.Utils;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractDAO<T extends AbstractModel> {

    public static final String SPECIAL_CHARACTER = "àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ·/_,:;&";
    public static final String REPLACE_CHARACTER = "aaaaaaaaaaaaaaaaaeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyd       ";

    @PersistenceContext
    protected EntityManager entityManager;

    public T getById(Class<T> clazz, Integer id) {
        return entityManager.find(clazz, id);
    }

    public List<?> getInPage(Class<T> clazz, int page, int limit) {
        if (page <= 0) page = 1;
        if (limit <= 0) limit = 12;
        return entityManager.createQuery("SELECT T FROM " + clazz.getName() + " T where T.displayToUser = true")
                .setFirstResult((page - 1) * limit) // only limit items in page
                .setMaxResults(limit).getResultList();
    }

    public List<?> getInPageForAdmin(Class<T> clazz, int page, int limit) {
        if (page <= 0) page = 1;
        if (limit <= 0) limit = 12;
        return entityManager.createQuery("SELECT T FROM " + clazz.getName() + " T")
                .setFirstResult((page - 1) * limit) // only limit items in page
                .setMaxResults(limit).getResultList();
    }

    public List<?> findByLikeAnotherString(Class<T> clazz, String field, String string) {
        return entityManager.createQuery("SELECT T FROM " + clazz.getName()
                + " T where TRANSLATE (lower(T." + field + ")," +
                "'" + SPECIAL_CHARACTER + "', '" + REPLACE_CHARACTER + "') " +
                "like '%" + Utils.toASCII(string) + "%'").getResultList();
    }

    public void save(T model) {
        try {
            entityManager.persist(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void merge(T model) {
        try {
            entityManager.merge(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(T model) {
        try {
            entityManager.remove(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getRowCount(Class<T> clazz) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(clazz)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @SuppressWarnings("rawtypes")
    public List findByCriteria(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getCurrentSession()).setProjection(null)
                .setResultTransformer(CriteriaSpecification.ROOT_ENTITY).list();
    }

    public Session getCurrentSession() {
        return (Session) this.entityManager.getDelegate();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean isStringUsed(Class<T> clazz, String attribute, String string) {
        try {
            DetachedCriteria c = DetachedCriteria.forClass(clazz).setProjection(Projections.rowCount())
                    .add(Restrictions.eq(attribute, string));
            int res = findByCriteria(c).size();
            return res != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
