package org.example.dao.impl;

import java.util.List;
import java.util.Optional;

import org.example.domain.Department;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.example.dao.GenericDao;
import org.example.domain.Employee;
import org.example.exception.DataProcessingException;
import org.example.util.HibernateUtil;

public class EmployeeDaoImpl implements GenericDao<Employee> {
    @Override
    public Employee save(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
            return employee;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert employee into DB: " + employee, e);
        }
    }

    @Override
    public Optional<Employee> find(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Employee.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't find employee by provided id: " + id, e);
        }
    }

    @Override
    public Employee update(Employee employee) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Employee> employeeFromDB
                    = Optional.ofNullable(session.get(Employee.class, employee.getId()));
            if (employeeFromDB.isPresent()) {
                session.merge(employee);
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find employee by provided id: "
                        + employee.getId());
            }
            return employeeFromDB.get();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update provided employee", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Employee> employee = Optional.ofNullable(session.get(Employee.class, id));
            if (employee.isPresent()) {
                session.remove(employee.get());
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find employee by provided id: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't delete department by provided id: " + id);
        }
    }

    @Override
    public List<Employee> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Employee e", Employee.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find all employees ", e);
        }
    }
}
