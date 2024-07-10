//package sg.nus.iss.com.Leaveapp.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import sg.nus.iss.com.Leaveapp.model.Employee;
//import sg.nus.iss.com.Leaveapp.model.LeaveType;
//
//public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
//	@Query("SELECT e FROM Employee e WHERE e.id = :id")
//	public Employee findEmployeeRoleById(@Param("id") Long id);
//	
//	@Query("SELECT l FROM LeaveType l WHERE l.id = :id")
//	LeaveType findLeaveTypeById(@Param("id")Long id);
//}
