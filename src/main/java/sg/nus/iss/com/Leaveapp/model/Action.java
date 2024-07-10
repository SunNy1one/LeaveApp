package sg.nus.iss.com.Leaveapp.model;

import java.util.*;
import java.util.stream.Stream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import sg.nus.iss.com.Leaveapp.Exceptions.TypeNotFoundException;


public class Action {

	private Long id;
	
	private String name;
	
	private String urlPattern;
	
	private List<String> roles;

	public Action(Long id, String name, String urlPattern, List<String> roles) {
		super();
		this.id = id;
		this.name = name;
		this.urlPattern = urlPattern;
		this.roles = roles;
	}
	
	public Action() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getDisplayName() {
		return Stream.of(this.name.split("_")).map(s -> (s.charAt(0) + "").toUpperCase() + s.substring(1)).reduce((s1, s2) -> s1 + " " + s2).get();
	}
	
	public static final Action actionSubmitApplication = new Action(1L, "submit_leave_application", "/leave/saveForm", List.of("employee", "admin", "manager"));
	public static final Action actionManageLeave = new Action(2L, "manage_leave_application", "/leave/manage-leave", List.of("employee", "admin", "manager"));
	public static final Action actionClaimCompensation = new Action(3L, "claim_compensation", "/claim/make-claim", List.of("employee", "admin", "manager"));
	public static final Action actionLeaveHistory = new Action(4L, "view_leave_history", "/leave/viewleaveHistory", List.of("employee", "admin", "manager"));
	public static final Action actionViewEmployeeApplications = new Action(6L, "view_employee_applications", "/manager/applications", List.of("manager"));
	public static final Action actionViewEmployeeHistory = new Action(7L, "view_employee_leave_history", "/manager/employeeHistory", List.of("manager"));
	public static final Action actionManageLeaveEntitlement = new Action(10L, "manage_leave_entitlement", "/adminEntitlement/leavetypes", List.of("admin"));
	public static final Action actionManageStaff = new Action(11L, "manage_staff", "/admin/employees", List.of("admin"));
	public static final Action actionManageHolidays = new Action(11L, "manage_holidays", "/admin/holidays", List.of("admin"));
	public static final Action actionConsumeClaim = new Action(12L, "consume_claim", "/leave/consume", List.of("employee", "admin", "manager"));
	
	public static List<Action> getAllActions(){
		List<Action> allActions = new ArrayList<Action>();
		allActions.add(actionSubmitApplication);
		allActions.add(actionManageLeave);
		allActions.add(actionClaimCompensation);
		allActions.add(actionConsumeClaim);
		allActions.add(actionLeaveHistory);
		allActions.add(actionViewEmployeeApplications);
		allActions.add(actionViewEmployeeHistory);
		allActions.add(actionManageLeaveEntitlement);
		allActions.add(actionManageStaff);
		allActions.add(actionManageHolidays);
		
		return allActions;
	}
	
	public static List<Action> getEmployeeActions(){
		return getAllActions().stream().filter(a -> a.getRoles().contains("employee")).toList();
	}
	
	public static List<Action> getAdminActions(){
		return getAllActions().stream().filter(a -> a.getRoles().contains("admin")).toList();
	}
	
	public static List<Action> getManagerActions(){
		return getAllActions().stream().filter(a -> a.getRoles().contains("manager")).toList();
	}
	
	public static List<Action> getActionByRole(String role) throws TypeNotFoundException{
		switch (role) {
		case "employee":
			return getEmployeeActions();
		case "admin":
			return getAdminActions();
		case "manager":
			return getManagerActions();
		default:
			throw new TypeNotFoundException();
		}
	}
}
