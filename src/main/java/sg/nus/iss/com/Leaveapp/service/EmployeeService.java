package sg.nus.iss.com.Leaveapp.service;


import java.util.List;

import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Role;

public interface EmployeeService {
	
	public Employee findEmployeeByUsername(String username);
	
	public Employee findEmployeeRoleById(Long id);
	
	public Employee findEmployeeById(Long id);
	
	

}
