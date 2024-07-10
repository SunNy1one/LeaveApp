package sg.nus.iss.com.Leaveapp.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.nus.iss.com.Leaveapp.model.Action;
import sg.nus.iss.com.Leaveapp.model.Claim;
import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.model.LeaveType;
import sg.nus.iss.com.Leaveapp.repository.EmployeeRepository;
import sg.nus.iss.com.Leaveapp.repository.LeaveEntitlementRepository;
import sg.nus.iss.com.Leaveapp.service.LeaveEntitlementServiceImpl;
import sg.nus.iss.com.Leaveapp.service.LeaveService;
import sg.nus.iss.com.Leaveapp.validator.LeaveValidator;

@Controller
@RequestMapping("/leave")
public class LeaveController {
	
	@Autowired
	private LeaveService leaveService;
	
	//No service layer for employee?
	@Autowired
	private EmployeeRepository employeeService;
	
	//No service layer for leaveType?
	@Autowired
	private LeaveEntitlementServiceImpl leaveEntitlementService;
	
	@Autowired
	private LeaveValidator leaveValidator;
	
	@InitBinder
	private void initCourseBinder(WebDataBinder binder) {
		binder.addValidators(leaveValidator);
	}
	
	@PostMapping("/submitForm")
	public String submitLeave(@Valid @ModelAttribute("leave") Leave leave, BindingResult bindingResult, HttpSession session, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "leaveSubmitForm");
			List<LeaveEntitlement> leaveEntitlements = leaveEntitlementService.getLeaveEntitlementsTypesByRole(leave.getEmployee().getRole().getName());
			model.addAttribute("leaveEntitlements", leaveEntitlements);
			return "index";
		}
		leave.setStatus(LeaveStatus.Applied);
		Leave leaveSaved = leaveService.save(leave);
		if(leaveSaved == null) {
			model.addAttribute("action", "error-message");
			model.addAttribute("error", "Leave submission failed.");
		} else {
			model.addAttribute("action", "show-message");
			model.addAttribute("message", "Leave saved successfully.");
		}
		
		return "index";
	}
	
	@GetMapping("/saveForm")
	public String leaveForm(Model model, HttpSession session) {
		Leave leaveToApply = new Leave();
		Employee e = (Employee)session.getAttribute("loggedInEmployee");
		List<LeaveEntitlement> leaveEntitlements = leaveEntitlementService.getLeaveEntitlementsTypesByRole(e.getRole().getName());
		leaveToApply.setEmployee(e);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		leaveToApply.setEntitlement(leaveEntitlementService.findLeaveEntitlementByType("annual", e.getRole().getId(), currentYear));
		model.addAttribute("leave", leaveToApply);
		
		model.addAttribute("leaveEntitlements", leaveEntitlements);
		model.addAttribute("action", "leaveSubmitForm");
		model.addAttribute("employeeName", e.getName());
		model.addAttribute("employeeId", e.getId());
		return "index"; // 
	}
	
	
	@PostMapping("/submitHistory")
	public String submitHistoryView(@RequestParam("employeeId") Long employeeId, RedirectAttributes redirectAttributes)
	{
		redirectAttributes.addAttribute("id", employeeId);
		return "redirect:/leave/viewLeaveHistory";
	}
	
	
	@GetMapping("/viewleaveHistory")
	public String leaveHistoryChecker(Model model, HttpSession session)
	{
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
		List<Leave> leaveHistory = leaveService.findLeavesFromEmployeeId(loggedInEmployee.getId());
		model.addAttribute("leaveHistory", leaveHistory);
		model.addAttribute("action", "viewleaveHistory");
		
		List<Claim> claimHistory = leaveService.findClaimsByEmployee(loggedInEmployee);
		model.addAttribute("claimHistory", claimHistory);
		return "index";
	}
	
	@GetMapping("/manage-leave")
    public String manageLeave(Model model, HttpSession session) {
		Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
        List<Leave> Leaves = leaveService.findLeavesFromEmployeeId(loggedInEmployee.getId());
        
        model.addAttribute("leaves", Leaves);
        model.addAttribute("action", "manage-leave");
        return "index";
    }
	
	@GetMapping("/update-leave/{id}")
    public String updateLeave(@PathVariable("id") Long id, Model model) {
        Leave leave = leaveService.findById(id);
		List<LeaveEntitlement> leaveEntitlements = leaveEntitlementService.getLeaveEntitlementsTypesByRole(leave.getEmployee().getRole().getName());
		
		model.addAttribute("leaveEntitlements", leaveEntitlements);
        model.addAttribute("leave", leave);
        model.addAttribute("action", "update-leave");
        return "index";
    }

    @PostMapping("/update-leave")
    public String updateLeave(@Valid @ModelAttribute("leave") Leave leave, Model model, HttpSession session) {
    	if (leave != null && (LeaveStatus.Applied == leave.getStatus() || LeaveStatus.Updated == leave.getStatus())) {
            leave.setStatus(LeaveStatus.Updated);
            leaveService.save(leave);
        }
        return "redirect:/leave/viewleaveHistory";
    }

    @RequestMapping("/delete-leave/{id}")
    public String deleteLeave(@PathVariable("id") Long id) {
        Leave Leave = leaveService.findById(id);
        if (Leave != null && (LeaveStatus.Applied == Leave.getStatus() || LeaveStatus.Updated == Leave.getStatus())) {
            Leave.setStatus(LeaveStatus.Deleted);
            leaveService.save(Leave);
        }
        return "redirect:/leave/viewleaveHistory";
    }

    @RequestMapping("/cancel-leave/{id}")
    public String cancelLeave(@PathVariable("id") Long id) {
        Leave Leave = leaveService.findById(id);
        if (Leave != null && LeaveStatus.Approved == Leave.getStatus()) {
        	Leave.setStatus(LeaveStatus.Cancelled);
            leaveService.save(Leave);
        }
        return "redirect:/leave/viewleaveHistory";
    }
    

    public Double ComputeLeaveBalance(Employee employee, Leave leave) {
    	List<Leave> existingLeaves = leaveService.findLeavesFromEmployeeId(leave.getEmployee().getId());
		List<Leave> consumedLeaves = existingLeaves
				.stream()
				.filter(l -> l.isConsumedOrConsuming())
				.toList();
		return leave.getEntitlement().getNumberOfDays() - Leave.consumedDaysOfLeave(consumedLeaves, leave.getEntitlement().getLeaveType());
    }
    
    @GetMapping("/consume")
    public String consumeClaim(Model model, HttpSession session) {
    	LeaveEntitlement compensationEntitlement = leaveEntitlementService.getCompensationEntitlement();
    	Employee e = (Employee)session.getAttribute("loggedInEmployee");
    	Leave leaveUsingClaim = new Leave();
    	
    	if(leaveService.hasUnconsumeClaimedLeaves(e)) {
    		leaveUsingClaim.setEmployee(e);
    		leaveUsingClaim.setEntitlement(compensationEntitlement);
        	model.addAttribute("leave", leaveUsingClaim);
        	model.addAttribute("action", "consume-claim");
    	} else {
    		model.addAttribute("error", "You do not have approved claim");
        	model.addAttribute("action", "error-message");
    	}
    	return "index";
    	
    }
	
	@PostMapping("/applyConsumption")
    public String applyConsumption(@Valid @ModelAttribute("leave") Leave leave, BindingResult bindingResult, Model model, HttpSession session) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("action", "consume-claim");
			return "index";
		}
		LeaveEntitlement compensationEntitlement = leaveEntitlementService.getCompensationEntitlement();
		Employee e = (Employee)session.getAttribute("loggedInEmployee");
		
		leave.setEmployee(e);
		leave.setEntitlement(compensationEntitlement);
    	leave.setStatus(LeaveStatus.Applied);
		Leave leaveSaved = leaveService.save(leave);
    	if(leaveSaved == null) {
			model.addAttribute("action", "error-message");
			model.addAttribute("error", "Leave submission failed.");
		} else {
			model.addAttribute("action", "show-message");
			model.addAttribute("message", "Leave saved successfully.");
		}
    	return "index";
    }
    
}

