package sg.nus.iss.com.Leaveapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.repository.EmployeeRepository;

@Service
public class LoginServiceImp implements LoginService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public Employee login(String username, String password) {
		Employee employee = employeeRepository.findEmployeeByUsername(username);
		if(employee != null && employee.getPassword().compareTo(password) == 0) {
			return employee;
		}
		return null;
	}
}
