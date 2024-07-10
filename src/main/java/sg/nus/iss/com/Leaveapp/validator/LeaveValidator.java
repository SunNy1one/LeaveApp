package sg.nus.iss.com.Leaveapp.validator;


import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.nus.iss.com.Leaveapp.model.Claim;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.service.LeaveService;

@Component
public class LeaveValidator implements Validator{

	@Autowired
	private LeaveService leaveService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Leave.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Leave leave = (Leave) target;
		
		List<Leave> existingLeaves = leaveService.findLeavesFromEmployeeId(leave.getEmployee().getId());
		List<Claim> approvedClaims = leaveService.findApprovedClaimsByEmployee(leave.getEmployee());
		List<Leave> consumedLeaves;
		if(leave.getId() == null) {
			consumedLeaves = existingLeaves
					.stream()
					.filter(l -> l.isConsumedOrConsuming())
					.toList();
		} else {
			consumedLeaves = existingLeaves
					.stream()
					.filter(l -> l.isConsumedOrConsuming() && l.getId().compareTo(leave.getId()) != 0)
					.toList();
		}
		
		if(leave.getStart().getDayOfWeek().compareTo(DayOfWeek.SATURDAY) == 0 || leave.getStart().getDayOfWeek().compareTo(DayOfWeek.SUNDAY) == 0) {
			errors.rejectValue("start", "error.start", "Start date must be working day.");
		}
		if(leave.getEntitlement().getLeaveType().compareTo("compensation") != 0 || !leave.isHalfDayLeave()) {
			if(leave.getStart().compareTo(leave.getEnd()) > 0) {
				errors.rejectValue("start", "error.start", "Start date must be before end date.");
			}
			if(leave.getEnd().getDayOfWeek().compareTo(DayOfWeek.SATURDAY) == 0 || leave.getEnd().getDayOfWeek().compareTo(DayOfWeek.SUNDAY) == 0) {
				errors.rejectValue("end", "error.end", "End date must be working day.");
			}
		} else {
			if(leave.getHalfOfDay() == null) {
				errors.rejectValue("halfDayLeave", "error.halfDayLeave", "Please indicate AM or PM leave.");
			}
		}
		if(leave.isOverlappedWith(consumedLeaves)) {
			errors.rejectValue("end", "error.end", "Leave date overlapped with existing leaves.");
		}

		Double balance;
		Double consumedClaims;
		if(leave.getEntitlement().getLeaveType().compareTo("compensation") != 0) {
			consumedClaims = Leave.consumedDaysOfLeave(consumedLeaves, leave.getEntitlement().getLeaveType());
			balance = leave.getEntitlement().getNumberOfDays() -  consumedClaims;
		} else {
			Double claimedCompensation = approvedClaims
					.stream()
					.map(c -> c.getApprovedDays())
					.reduce((d1, d2) -> d1 + d2)
					.orElse(0.0);
			consumedClaims = Leave.consumedDaysOfLeave(consumedLeaves, leave.getEntitlement().getLeaveType());
			balance = claimedCompensation - consumedClaims;
		}
		if(balance < leave.getNumberOfDays()) {
			System.out.println("Number of days exceed leave balance. Balance: " + balance + ". Consumed: " + consumedClaims);
			errors.rejectValue("numberOfDays", "error.numberOfDays", "Number of days applying exceed leave balance.");
		}

	}

}
