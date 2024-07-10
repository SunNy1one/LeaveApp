package sg.nus.iss.com.Leaveapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.com.Leaveapp.model.Claim;
import sg.nus.iss.com.Leaveapp.model.Employee;

public interface ClaimRepository extends JpaRepository<Claim, Long>{
	
	List<Claim> findClaimsByEmployee(Employee employee);

	@Query("SELECT c FROM Claim c WHERE c.employee.id = :employee_id AND c.status = 3")
	List<Claim> findApprovedClaimsByEmployee(@Param("employee_id") Long employee_id);
	
	@Query("SELECT c FROM Claim c WHERE c.status = :status AND c.employee.id in :reportee_ids")
	List<Claim> findClaimsByStatusAndReportees(@Param("status") int status, @Param("reportee_ids") List<Long> reportee_ids);
	
	@Query("SELECT c FROM Claim c WHERE c.status = :approvedStatus AND c.employee.id = :employee_id")
	List<Claim> findApprovedClaimsByEmployee(@Param("approvedStatus") int approvedStatus, @Param("employee_id") Long employee_id);
	
	@Query("SELECT c FROM Claim c WHERE c.employee.id = :employee_id")
	List<Claim> findClaimsByEmployee(@Param("employee_id") Long employee_id);
}
