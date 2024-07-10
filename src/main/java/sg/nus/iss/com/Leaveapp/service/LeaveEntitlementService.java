
package sg.nus.iss.com.Leaveapp.service;

import java.time.LocalDate;
import java.util.List;

import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
public interface LeaveEntitlementService {

    LeaveEntitlement createLeaveEntitlement(LeaveEntitlement leaveEntitlement);
    List<LeaveEntitlement> findAllLeaveEntitlements();
    LeaveEntitlement getLeaveEntitlementById(int id);
    void saveLeaveEntitlement(LeaveEntitlement leaveEntitlement);
    void deleteLeaveEntitlement(int id);
    
    
    LeaveEntitlement updateLeaveEntitlement(int id, LeaveEntitlement updatedEntitlement);

	boolean isWithinAnnualLeaveLimit(int employeeId, LocalDate startDate, LocalDate endDate);

	boolean isWithinMedicalLeaveLimit(int employeeId, LocalDate startDate, LocalDate endDate);

	boolean isValidLeavePeriod(LocalDate startDate, LocalDate endDate);
	
	LeaveEntitlement findLeaveEntitlementByType(String type, Long roleId, int year);
	
	List<String> getLeaveTypesByRole(String roleName);
	
	List<LeaveEntitlement> getLeaveEntitlementsTypesByRole(String roleName);
	
	LeaveEntitlement getCompensationEntitlement();
}