package sg.nus.iss.com.Leaveapp.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import jakarta.validation.Valid;
import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.service.AdminService;
import sg.nus.iss.com.Leaveapp.validator.LeaveEntitlementValidator;

@Controller
@RequestMapping("/adminEntitlement")
public class AdminEntitlementController {

	@Autowired
    private AdminService adminService;
	
	@Autowired
	private LeaveEntitlementValidator leaveEntitlementValidator;
	
	@InitBinder
	private void initCourseBinder(WebDataBinder binder) {
		binder.addValidators(leaveEntitlementValidator);
	}
	
	// Manage Leave Types
    @GetMapping("/leavetypes")
    public String getAllLeaveTypes(@RequestParam(value = "year", required = false) Integer year, Model model) {
    	List<LeaveEntitlement> leaveEntitlements;
        if (year != null) {
            leaveEntitlements = adminService.getLeaveEntitlementsByYear(year);
        } else {
            leaveEntitlements = adminService.getAllLeaveEntitlements();
        }
        model.addAttribute("leaveEntitlements", leaveEntitlements);
        model.addAttribute("action", "leavetypes");
        model.addAttribute("years", getYears());  // Populate years for the dropdown
        return "index";
    }
	
	@GetMapping("/leavetypes/add")
    public String addLeaveTypeForm(Model model) {
        model.addAttribute("leaveEntitlement", new LeaveEntitlement(0, 0));
        model.addAttribute("roles", adminService.getAllRoles());
        model.addAttribute("years", getYears());  // Populate years for the dropdown
        model.addAttribute("action", "add-leave-type");
        return "index";
    }
    
    @PostMapping("/leavetypes/save")
    public String saveLeaveType1(@Valid @ModelAttribute("leaveEntitlement") LeaveEntitlement entitlement, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "add-leave-type");
            model.addAttribute("roles", adminService.getAllRoles());
            model.addAttribute("years", getYears());
            return "index";
        }

        LeaveEntitlement annualLeaveEntitlement = new LeaveEntitlement("annual", entitlement.getAnnualLeave(), entitlement.getRole(), entitlement.getYear());
        LeaveEntitlement sickLeaveEntitlement = new LeaveEntitlement("medical", entitlement.getSickLeave(), entitlement.getRole(), entitlement.getYear());

        adminService.createOrUpdateLeaveType(annualLeaveEntitlement);
        adminService.createOrUpdateLeaveType(sickLeaveEntitlement);

        return "redirect:/adminEntitlement/leavetypes";
    }
    
    private List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 5; i++) {
            years.add(i);
        }
        return years;
    }
   
    @GetMapping("/leavetypes/edit/{id}")
    public String editLeaveTypeForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("leaveEntitlement", adminService.getLeaveEntitlementById(id));
        model.addAttribute("roles", adminService.getAllRoles());
        model.addAttribute("action", "edit-leave-type");
        return "index";
    }
    
    @PostMapping("/leavetypes/edit/save")
    public String editLeaveTypeForm(@Valid @ModelAttribute("leaveEntitlement") LeaveEntitlement leaveEntitlement, Model model, BindingResult bindingResult) {
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("roles", adminService.getAllRoles());
            model.addAttribute("action", "edit-leave-type");

    		return "index";
    	}
    	adminService.createOrUpdateLeaveType(leaveEntitlement);
        model.addAttribute("action", "leavetypes");
        return "redirect:/adminEntitlement/leavetypes";
    }
    
    @GetMapping("/leavetypes/delete/{id}")
    public String deleteLeaveType(@PathVariable Long id, Model model) {
        try {
        	adminService.deleteLeaveEntitlement(id);
            return "redirect:/adminEntitlement/leavetypes";
        } catch(DataIntegrityViolationException e) {
        	e.printStackTrace();
        	model.addAttribute("action", "error-message");
        	model.addAttribute("error", "Cannot delete entitlement due to existing leave of the given type.");
        	return "index";
        } catch(Exception e) {
        	e.printStackTrace();
        	model.addAttribute("action", "error-message");
        	model.addAttribute("error", "Cannot delete entitlement. " + e.getClass().toString());
        	return "index";
        }
    }
}
