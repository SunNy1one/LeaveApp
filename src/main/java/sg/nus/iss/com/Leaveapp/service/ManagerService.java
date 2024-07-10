package sg.nus.iss.com.Leaveapp.service;

import java.util.List;

import sg.nus.iss.com.Leaveapp.model.Claim;
import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveType;

public interface ManagerService {
    // Employee methods
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    List<Leave> getLeaveApplicationsForApproval(Long managerId);
    List<Claim> getClaimRequestsForApproval(Long managerId);
    List<Leave> getEmployeeLeaveHistory(Employee employee);
	List<Claim> getEmployeeClaimHistory(Employee employee);
    Employee findEmployeeByName(String employeeName);
	List<Employee> findReporteeEmployeeByManagerId(Long manager_id);
	List<Employee> findEmployeesByName(String name);
	
    // Leave methods
	Leave getLeaveApplicationById(Long id);
    Claim getClaimById(Long id);
    
    //approve or reject Leave
    Leave approveLeaveApplication(Long leaveApplicationId, String comment);
    Leave rejectLeaveApplication(Long leaveApplicationId, String comment);

	
	

}
