package org.example.dao.impl;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.example.dao.GenericDao;
import org.example.domain.Task;
import org.example.exception.DataProcessingException;
import org.example.util.HibernateUtil;

public class TaskDaoImpl implements GenericDao<Task> {
    @Override
    public Task save(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
            return task;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert task into DB: " + task, e);
        }
    }

    @Override
    public Optional<Task> find(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Optional<Task> taskFromDB
                    = Optional.ofNullable(session.get(Task.class, id));
            if (taskFromDB.isPresent()) {
                Hibernate.initialize(taskFromDB.get().getTaskEmployees());
            }
            return taskFromDB;
        } catch (Exception e) {
            throw new DataProcessingException("Can't find task by provided id: " + id, e);
        }
    }

    @Override
    public Task update(Task task) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Task> taskFromDb = Optional.ofNullable(session.find(Task.class, task.getId()));
            if (taskFromDb.isPresent()) {
                session.merge(task);
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find task by provided id: " + task.getId());
            }
            return taskFromDb.get();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't update provided task", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Optional<Task> task = Optional.ofNullable(session.get(Task.class, id));
            if (task.isPresent()) {
                session.remove(task.get());
                transaction.commit();
            } else {
                throw new DataProcessingException("Can't find task by provided id: " + id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't delete task by provided id: " + id);
        }
    }

    @Override
    public List<Task> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("FROM Task t LEFT JOIN FETCH taskEmployees ", Task.class)
                    .getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find all tasks ", e);
        }
    }
}
