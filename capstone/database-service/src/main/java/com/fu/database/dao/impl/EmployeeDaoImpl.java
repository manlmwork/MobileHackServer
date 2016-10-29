package com.fu.database.dao.impl;

import com.fu.database.dao.EmployeeDao;
import com.fu.database.entity.Employee;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by manlm on 7/23/2016.
 */
@Repository
public class EmployeeDaoImpl extends GenericDaoImpl<Employee, Integer> implements EmployeeDao {

    private static final Logger LOG = Logger.getLogger(EmployeeDaoImpl.class);

    /**
     * Get Admin Account by username
     *
     * @param username
     * @return
     */
    @Override
    public Employee getByUsername(String username) {
        LOG.info("[getByUsername] Start: username = " + username);
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> root = cq.from(Employee.class);
        cq.where(cb.equal(root.get("username"), username));
        List<Employee> list = getEntityManager().createQuery(cq).getResultList();
        if (!list.isEmpty()) {
            LOG.info("[getByUsername] End");
            return list.get(0);
        }
        LOG.info("[getByUsername] End");
        return null;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Employee> getAll() {
        LOG.info("[getAll] Start");
        Query query = getEntityManager().createQuery("FROM " + Employee.class.getSimpleName());
        LOG.info("[getAll] End");
        return query.getResultList();
    }

    @Override
    public int updateAccount(String username,String firstName, String lastName, String phone) {
        LOG.info("[updateAccount] Start");
        Query query = getEntityManager().createQuery("UPDATE " + Employee.class.getSimpleName()+ " e set e.firstName =:firstName , e.lastName =:lastName, e.phone =:phone WHERE e.username =:username");
        query.setParameter("username",username);
        query.setParameter("firstName",firstName);
        query.setParameter("lastName",lastName);
        query.setParameter("phone",phone);
        LOG.info("[updateAccount] End");
        return query.executeUpdate();
    }

    @Override
    public boolean checkEmailExist(String email) {
        LOG.info("[checkEmailExist] Start");
        Query query = getEntityManager().createQuery("FROM " + Employee.class.getSimpleName() + " a Where a.email=:email" );
        query.setParameter("email",email);
        LOG.info("[checkEmailExist] End");
        return !query.getResultList().isEmpty();
    }
}
