package sg.nus.iss.com.Leaveapp.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.service.AdminService;

@Component
public class LeaveEntitlementValidator implements Validator{

	@Autowired
	private AdminService adminService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LeaveEntitlement.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LeaveEntitlement entitlement = (LeaveEntitlement) target;
		
		LeaveEntitlement existingAnnualLeaveEntitlement = adminService.findLeaveEntitlementByRoleTypeAndYear(entitlement.getRole(), "annual", entitlement.getYear());
        LeaveEntitlement existingSickLeaveEntitlement = adminService.findLeaveEntitlementByRoleTypeAndYear(entitlement.getRole(), "medical", entitlement.getYear());

        if (existingAnnualLeaveEntitlement != null && entitlement.getAnnualLeave() != 0 &&
        	    (existingAnnualLeaveEntitlement.getRole().getName().compareTo(entitlement.getRole().getName()) == 0 &&
        	      existingAnnualLeaveEntitlement.getYear() == entitlement.getYear())) {
        	errors.rejectValue("annualLeave", "error.annualLeave", "Annual leave entitlement for this role and year already exists.");
        }

        if (existingSickLeaveEntitlement != null && entitlement.getSickLeave() != 0 &&
        	    (existingSickLeaveEntitlement.getRole().getName().compareTo(entitlement.getRole().getName()) == 0 &&
        	      existingSickLeaveEntitlement.getYear() == entitlement.getYear())) {
        	errors.rejectValue("sickLeave", "error.sickLeave", "Sick leave entitlement for this role and year already exists.");
        	}


        
	}

}
