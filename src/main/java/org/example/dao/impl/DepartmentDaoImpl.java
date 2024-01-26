package org.example.dao.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.example.dao.GenericDao;
import org.example.domain.Department;
import org.example.exception.DataProcessingException;
import org.example.util.HibernateUtil;

public class DepartmentDaoImpl implements GenericDao<Department> {
    @Override
    public Department save(Department department) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(department);
            transaction.commit();
            return department;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert department into DB: " + department, e);
        }
    }

    @Override
    public Optional<Department> find(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Department.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't find department by provided id: " + id, e);
        }
    }

    @Override
    public Department update(Department department) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Department> departmentFromDB
                    = Optional.ofNullable(session.get(Department.class, department.getId()));
            if (departmentFromDB.isPresent()) {
                session.merge(department);
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find department by provided id: "
                        + department.getId());
            }
            return departmentFromDB.get();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update department: " + department, e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Department> department = Optional.ofNullable(session.get(Department.class, id));
            if (department.isPresent()) {
                session.remove(department.get());
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find department by provided id: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't delete department by provided id: " + id);
        }
    }

    @Override
    public List<Department> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Department d", Department.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find all departments ", e);
        }
    }
}
