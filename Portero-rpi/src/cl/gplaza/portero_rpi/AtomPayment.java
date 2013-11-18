package cl.gplaza.portero_rpi;

import java.io.Serializable;

public class AtomPayment implements Serializable {
	
	private static final long serialVersionUID = -5435670920302756945L;
	
	private String name = "";
	private String password = "";
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AtomPayment(String name, String password) {
		this.setName(name);
		this.setPassword(password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}