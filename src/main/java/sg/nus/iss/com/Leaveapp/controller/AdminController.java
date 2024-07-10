package sg.nus.iss.com.Leaveapp.controller;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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


import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.service.AdminService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
 
    

    
    // Manage Employees
    
    @GetMapping("/employees")
    public String getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
    	Page<Employee> employeesPage = adminService.getAllEmployees(page, size);
        model.addAttribute("employees", employeesPage.getContent());
        model.addAttribute("currentPage", employeesPage.getNumber());
        model.addAttribute("totalPages", employeesPage.getTotalPages());
        model.addAttribute("totalItems", employeesPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("action", "employee");
        return "index";
    }

    
    @GetMapping("/employees/add")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("managers", adminService.getManagers());
        model.addAttribute("roles", adminService.getAllRoles()); // Fetching roles
        model.addAttribute("action", "add-employee");
        return "index";
    }
    
    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        model.addAttribute("employee", adminService.getEmployeeById(id));
        model.addAttribute("managers", adminService.getManagers());
        model.addAttribute("roles", adminService.getAllRoles()); // Fetching roles
        model.addAttribute("action", "edit-employee");
        return "index";
    }
    
    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        adminService.createOrUpdateEmployee(employee);
        return "redirect:/admin/employees";
    }
    
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model) {
    	try {
    		adminService.deleteEmployee(id);
            return "redirect:/admin/employees";
        } catch(DataIntegrityViolationException e) {
        	e.printStackTrace();
        	model.addAttribute("action", "error-message");
        	model.addAttribute("error", "Cannot delete employee due to employee's existing leave records.");
        	return "index";
        } catch(Exception e) {
        	e.printStackTrace();
        	model.addAttribute("action", "error-message");
        	model.addAttribute("error", "Cannot delete employee. " + e.getClass().toString());
        	return "index";
        }
    }
    
    
}