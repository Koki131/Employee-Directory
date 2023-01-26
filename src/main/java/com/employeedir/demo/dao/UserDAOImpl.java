package com.employeedir.demo.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.model.User;



@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public User findUserByName(String userName) {
		
		Session session = entityManager.unwrap(Session.class);
		
		Query<User> query = session.createQuery("from User where userName=:theName", User.class);
		query.setParameter("theName", userName);
		
		User user = null;
		
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
		
		
	}

	@Override
	public void save(User user) {
		Session session = entityManager.unwrap(Session.class);
		
		session.saveOrUpdate(user);
	}

	@Override
	public User findUserByEmail(String email) {
		Session session = entityManager.unwrap(Session.class);
		
		Query<User> query = session.createQuery("from User where email=:theEmail", User.class);
		query.setParameter("theEmail", email);
		
		
		User user = null;
		
		try {
			
			user = query.getSingleResult();
			
		} catch (Exception e) {
			user = null;
		}
		
		return user;
	}

}
