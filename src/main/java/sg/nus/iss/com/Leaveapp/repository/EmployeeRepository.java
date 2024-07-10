package sg.nus.iss.com.Leaveapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Role;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("SELECT e FROM Employee e WHERE e.username = :username")
	public Employee findEmployeeByUsername(@Param("username") String username);
	
	@Query("SELECT e FROM Employee e WHERE e.id = :id")
	public Employee findEmployeeRoleById(@Param("id") Long id);
	
	@Query("SELECT e FROM Employee e WHERE e.manager.id = :manager_id")
	public List<Employee> findReporteeEmployeesByManagerId(@Param("manager_id") Long id);
	
	public List<Employee> findByRole(Role role);

	@Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Employee> findByNameContainingIgnoreCase(@Param("name") String name);

	@Query("SELECT e FROM Employee e WHERE e.username = :username AND e.password = :password")
	Employee findEmployeeByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
