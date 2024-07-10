package sg.nus.iss.com.Leaveapp.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.service.LeaveApproveService;
import sg.nus.iss.com.Leaveapp.model.Claim;
import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveType;
import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.service.LeaveService;
import sg.nus.iss.com.Leaveapp.service.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {

	//extends StaffController 
	
	@Autowired
	private ManagerService managerService;
  
	@Autowired
	private LeaveApproveService leaveApproveService;

	@PostMapping("/approve")
	public String approveLeave(@ModelAttribute("leave") Leave leave, Model model) {
		leaveApproveService.approveLeave(leave);
		return "redirect:/manager/applications";
	}

	@PostMapping("/reject")
	public String rejectLeave(@ModelAttribute("leave") Leave leave) {
		leaveApproveService.rejectLeave(leave);
		return "redirect:/manager/applications";
	}
	
	@PostMapping("/approveClaim")
	public String approveClaim(@ModelAttribute("claim") Claim claim, Model model) {
		claim.setDateApproved(LocalDate.now());
		leaveApproveService.approveClaim(claim);
		return "redirect:/manager/applications";
	}

	@PostMapping("/rejectClaim")
	public String rejectLeave(@ModelAttribute("claim") Claim claim) {
		leaveApproveService.rejectClaim(claim);
		return "redirect:/manager/applications";
	}
	

	@GetMapping("/applications")
	public String viewApplicationsForApproval(Model model, HttpSession session) {
		Employee manager = (Employee) session.getAttribute("loggedInEmployee");
	    List<Leave> leaveApplications = managerService.getLeaveApplicationsForApproval(manager.getId());
	    model.addAttribute("leaveApplications", leaveApplications);
	    
	    List<Claim> claimRequests = managerService.getClaimRequestsForApproval(manager.getId());
	    model.addAttribute("claimRequests", claimRequests);
	    model.addAttribute("action", "leaveApplications");
	    return "index"; // Create a new HTML file for displaying the applications
	}
	
	
	@GetMapping("/claim-details/{id}")
	public String viewClaimDetails(@PathVariable("id") Long id, Model model) {
	    Claim claim = managerService.getClaimById(id);
	    model.addAttribute("claim", claim);
	    model.addAttribute("action", "claim-details");
	    return "index";
	}
	
	
	
	@GetMapping("/application-details/{id}")
	public String viewApplicationDetails(@PathVariable("id") Long id, Model model) {
	    Leave leaveApplication = managerService.getLeaveApplicationById(id);
	    model.addAttribute("leaveApplication", leaveApplication);
	    model.addAttribute("action", "application-details");
	    return "index";
	}
	
	
	@GetMapping("/employeeHistory")
	public String viewEmployeeLeaveHistory(Model model, HttpSession session) {
		Employee manager = (Employee) session.getAttribute("loggedInEmployee");
	    List<Employee> employees = managerService.findReporteeEmployeeByManagerId(manager.getId()); // Assuming you have a method to get all employees
	    model.addAttribute("employees", employees);
	    model.addAttribute("action", "employeeHistory");
	    return "index"; // Create a new HTML file for displaying the employees and their leave history
	}
	

	@PostMapping("/searchEmployee")
	public String searchEmployee(@RequestParam("employeeName") String employeeName, Model model, HttpSession session) {
	    Employee manager = (Employee) session.getAttribute("loggedInEmployee");
	    List<Employee> reporteeEmployee = managerService.findReporteeEmployeeByManagerId(manager.getId());

	    List<Employee> filteredEmployees = managerService.findEmployeesByName(employeeName).stream()
	            .filter(reporteeEmployee::contains)
	            .collect(Collectors.toList());

	    model.addAttribute("employeesFound", !filteredEmployees.isEmpty());
	    model.addAttribute("employees", filteredEmployees);
	    model.addAttribute("action", "search-employee-history");

	    return "index";
	}

	@GetMapping("/history/{id}")
	public String viewEmployeeLeaveHistory(@PathVariable("id") Long id, Model model) {
		Employee employee = managerService.getEmployeeById(id);
		List<Leave> leaveHistory = managerService.getEmployeeLeaveHistory(employee);
		List<Claim> claimHistory = managerService.getEmployeeClaimHistory(employee);
		model.addAttribute("employee", employee);
		model.addAttribute("leaveHistory", leaveHistory);
		model.addAttribute("claimHistory", claimHistory);
		model.addAttribute("action", "employee-leave-history");
		return "index";
	}

    @GetMapping("/leaveapprove/leave-applications")
    public String showLeaveApplications() {
        return "leave-applications";
    }

    @GetMapping("/leaveapprove/details/{id}")
    public String getDetail(@PathVariable("id") Long id, Model model) {
        Leave leave = leaveApproveService.getById(id);
        if (leave != null) {
            model.addAttribute("leaves", leaveApproveService.findLeavesByEmployeeIdOrderByIdDesc(leave.getEmployee().getId()));
            model.addAttribute("leave", leave);
            model.addAttribute("action", "show-leave-details");
            return "index";
        } else {
            model.addAttribute("leaves", leaveApproveService.findAllByOrderByIdDesc());
            return "index";
        }
    }
}

