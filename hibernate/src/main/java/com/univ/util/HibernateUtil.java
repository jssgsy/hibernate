package com.univ.util;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	static private Session session;
	static {
		session = new Configuration().configure().buildSessionFactory().openSession();
	}
	static public Session getSession(){
		return session;
	}
}
