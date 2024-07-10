
package sg.nus.iss.com.Leaveapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.nus.iss.com.Leaveapp.model.LeaveEntitlement;
import sg.nus.iss.com.Leaveapp.model.Role;


@Repository
public interface LeaveEntitlementRepository extends JpaRepository<LeaveEntitlement, Integer> {

	Optional<LeaveEntitlement> findById(Long id);
	void deleteById(Long id);
	
	@Query("SELECT e FROM LeaveEntitlement e WHERE e.leaveType = :type AND e.role.id =:role_id and e.year = :year")
	public LeaveEntitlement findLeaveEntitlementByType(@Param("type") String type, @Param("role_id") Long role_id,  @Param("year") int year);
	
	@Query("SELECT e.leaveType FROM LeaveEntitlement e WHERE e.role.name= :role")
	public List<String> getLeaveTypesByRole(@Param("role") String role);
	
	@Query("SELECT le FROM LeaveEntitlement le WHERE le.role = :role AND le.leaveType = :type AND le.year = :year")
	public LeaveEntitlement findLeaveEntitlementByRoleTypeAndYear(@Param("role") Role role, @Param("type") String type, @Param("year") Integer year);
	
	@Query("SELECT le FROM LeaveEntitlement le WHERE le.role.name = :roleName")
	public List<LeaveEntitlement> getLeaveEntitlementsTypesByRole(@Param("roleName") String roleName);
	
	
	public List<LeaveEntitlement> findByYear(Integer year);
	
	@Query("SELECT e FROM LeaveEntitlement e WHERE e.leaveType = 'compensation'")
	public LeaveEntitlement getCompensationEntitlement();
}

