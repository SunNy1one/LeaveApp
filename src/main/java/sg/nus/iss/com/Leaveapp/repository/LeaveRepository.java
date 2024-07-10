package sg.nus.iss.com.Leaveapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Leave;
import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.LeaveStatus;
import sg.nus.iss.com.Leaveapp.model.LeaveType;

import sg.nus.iss.com.Leaveapp.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Integer>, JpaSpecificationExecutor<Leave>{
	
		@Query("SELECT l from Leave l WHERE l.status = 3 AND l.startMonth =  :month")
		public List<Leave> findAllApprovedLeavesByMonth(@Param("month") int month);
		//Find the employee from leaveApplication id
		@Query("SELECT l.employee FROM Leave l WHERE l.id = :id")
		public Employee findEmployeeById(@Param("id") Long id);
		
		//Find start date from leaveApplication id
		@Query("SELECT l.start FROM Leave l WHERE l.id = :id")
		public LocalDate findStartDateById(@Param("id") Long id);
		
		//find end date From leaveApplication id
		@Query("SELECT l.end FROM Leave l WHERE l.id = :id")
		public LocalDate findEndDateById(@Param("id") Long id);
		
		// Find list of employees who took leave between start date and end date
		@Query("SELECT e FROM Leave l JOIN l.employee e WHERE l.start >= :startDate AND l.end <= :endDate")
		public List<Employee> findEmployeesBetweenStartAndEndDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

		//Find reasons from leaveApplicaion id
		@Query("SELECT l.reasons FROM Leave l WHERE l.id = :id")
		public String findReasonById(@Param("id") Long id);
		
		//Find leave status from leaveApplication id
		@Query("SELECT l.status FROM Leave l WHERE l.id = :id")
		public LeaveStatus findLeaveStatusById(@Param("id") Long id);
		
		//Find leave Type from leaveApplication id
		@Query("SELECT l.entitlement FROM Leave l WHERE l.id = :id")
		public LeaveEntitlement findLeaveEntitlementById(@Param("id") Long id);
		
		//find the leave application id from employee id
		@Query("SELECT l.id FROM Leave l JOIN l.employee e WHERE e.id = :empId")
		public Long findLeaveAppIdByEmpId(@Param("empId") Long empId);
		
		//find leave application reasons from employee id
		@Query("SELECT l.reasons FROM Leave l JOIN l.employee e WHERE e.id = :empId")
		public String findLeaveAppReasonsByEmpId(@Param("empId") Long empId);
		
		//find leave application start date from employee id
		@Query("SELECT l.start FROM Leave l JOIN l.employee e WHERE e.id = :empId")
		public LocalDate findLeaveAppStartDateByEmpId (@Param("empId") Long empId);
		
		//find leave application end date from employee id
		@Query("SELECT l.end FROM Leave l JOIN l.employee e WHERE e.id = :empId")
		public LocalDate findLeaveAppEndDateByEmpId (@Param("empId") Long empId);
		
		//find leave application status from employee id
		@Query("SELECT l.status FROM Leave l JOIN l.employee e WHERE e.id = :empId")
		public LeaveStatus findLeaveappStatusByEmpId (@Param("empId") Long empId);
		
		//find employee name from leave application id
		@Query("SELECT e.name FROM Leave l JOIN l.employee e WHERE l.id = :id")
		public String findEmpNameByLeaveId (@Param("id") Long id);
		
		//find employee role from leave application id
		@Query("SELECT e.role FROM Leave l JOIN l.employee e WHERE l.id = :id")
		public String findEmpRoleByLeaveId (@Param("id") Long id);
		
		//fine leaves from employee id
		@Query("SELECT l FROM Leave l WHERE l.employee.id = :id")
		public List<Leave> findLeavesFromEmployeeId(@Param("id") Long id);
		
		

		public List<Leave> findByStatusIn(List<Integer> asList);

		public List<Leave> findByEmployeeOrderByStartDesc(Employee employee);
		

		public Leave findById(Long id);
		
		@Query("SELECT l.employee FROM Leave l WHERE l.employee.name = :name")
		public Employee findEmployeeName(@Param("name") String name);
		
		@Query("SELECT l FROM Leave l WHERE l.employee.id = :employee_id AND l.entitlement.id = :compensation_id")
		public List<Leave> findCompensationLeavesByEmployee(@Param("employee_id") Long employee_id, @Param("compensation_id") int compensation_id);
}
