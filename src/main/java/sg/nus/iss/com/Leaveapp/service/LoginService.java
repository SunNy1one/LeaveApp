package sg.nus.iss.com.Leaveapp.service;

import sg.nus.iss.com.Leaveapp.model.Employee;

public interface LoginService {
	
	Employee login(String username, String password);

}
