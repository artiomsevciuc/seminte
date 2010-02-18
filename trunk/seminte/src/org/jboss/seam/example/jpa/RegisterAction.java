//$Id: RegisterAction.java 5509 2007-06-25 16:19:40Z gavin $
package org.jboss.seam.example.jpa;

import static org.jboss.seam.ScopeType.EVENT;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;

import md.cedacrinternational.utils.CryptUtils;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;

@Scope(EVENT)
@Name("register")
public class RegisterAction {

	@In
	private User user;

	@In
	private EntityManager em;

	@In
	private FacesMessages facesMessages;

	private String verify;

	private boolean registered;

	@SuppressWarnings("unchecked")
	public void register() {
		em.getTransaction().begin();
		if (user.getPassword().equals(verify)) {
			List existing = em.createQuery(
					"select u.username from User u where u.username=#{user.username}")
					.getResultList();
			if (existing.size() == 0) {
				try {
					user.setPassword(CryptUtils.encryptPassword(user.getPassword()));
				} catch (NoSuchAlgorithmException e) {
					facesMessages.add(e.getMessage());
				}
				em.persist(user);
				em.getTransaction().commit();
				facesMessages.add("Successfully registered as #{user.username}");
				registered = true;
			} else {
				facesMessages.addToControl("username",
						"Username #{user.username} already exists");
			}
		} else {
			facesMessages.add("verify", "Re-enter your password");
			verify = null;
		}
	}

	public void invalid() {
		facesMessages.add("Please try again");
	}

	public boolean isRegistered() {
		return registered;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

}
