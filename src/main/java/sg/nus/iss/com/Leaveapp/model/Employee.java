package sg.nus.iss.com.Leaveapp.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name="employees")
public class Employee {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @JsonIgnoreProperties("employees")
    @ManyToOne(fetch=FetchType.EAGER, cascade={})
    private Role role;
    
    @NotBlank
    private String username;
    
    private String password;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private Employee manager;
    
    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employee> reportees;
    
    @OneToMany(mappedBy = "employee", fetch=FetchType.EAGER)
	private List<Claim> claims;
    
    public List<Employee> getReportees() {
		return reportees;
	}

	public void setReportees(List<Employee> reportees) {
		this.reportees = reportees;
	}

	public Employee(String username, String password, String name, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.role = role;
	}    

    public Employee() {}
    
    // Getters and Setters
    
	public Long getId() { return id; }
    
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    
    public void setRole(Role role) { this.role = role; }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	
}