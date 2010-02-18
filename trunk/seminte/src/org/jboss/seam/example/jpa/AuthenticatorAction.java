package org.jboss.seam.example.jpa;

import static org.jboss.seam.ScopeType.SESSION;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;

import md.cedacrinternational.utils.CryptUtils;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class AuthenticatorAction {
	@In
	EntityManager em;

	@Out(required = false, scope = SESSION)
	private User user;
	
	@In
	Identity identity;

	@SuppressWarnings("unchecked")
	public boolean authenticate() {
		try {
			List results = em
					.createQuery(
							"select u from User u where u.username=#{identity.username} and u.password=:password")
					.setParameter("password",
							CryptUtils.encryptPassword(identity.getCredentials().getPassword()))
					.getResultList();

			if (results.size() == 0) {
				return false;
			} else {
				user = (User) results.get(0);
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			return false;
		}

	}
}
