package sg.nus.iss.com.Leaveapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImp implements EmployeeService{

	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	public Employee findEmployeeByUsername(String username) {
		return employeeRepository.findEmployeeByUsername(username);
	}

	@Override
	public Employee findEmployeeRoleById(Long id) {
		// TODO Auto-generated method stub
		return employeeRepository.findEmployeeRoleById(id);
	}

	@Override
	public Employee findEmployeeById(Long id) {
		return employeeRepository.findById(id).orElse(null);
	}

}
