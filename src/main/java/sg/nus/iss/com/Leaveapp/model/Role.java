package sg.nus.iss.com.Leaveapp.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="role")
	private List<Employee> employees;
	
	@OneToMany(mappedBy="role")
	private List<LeaveEntitlement> entitlements;

	public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Role() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<LeaveEntitlement> getEntitlements() {
		return entitlements;
	}

	public void setEntitlements(List<LeaveEntitlement> entitlements) {
		this.entitlements = entitlements;
	}
	
	public static final Role employeeRole = new Role(1L, "employee");
	public static final Role adminRole = new Role(2L, "admin");
	public static final Role managerRole = new Role(3L, "manager");
	public static final Role anyRole = new Role(4L, "any");
	
	public static final Role getRoleByString(String role) {
		if(role=="employee") {
			return employeeRole; 
		} else if(role=="admin") {
			return adminRole;
		} else if (role=="manager") {
			return managerRole;
		}
		return null;
	}
	
}
