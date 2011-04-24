package info.joseluismartin.hibernate.aop;

import org.hibernate.Session;

public interface SessionProcessor {

	void processSession(Session session);
}
