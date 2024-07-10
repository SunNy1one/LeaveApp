package sg.nus.iss.com.Leaveapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.com.Leaveapp.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	@Query("SELECT r FROM Role r WHERE r.name = :name")
	public Role findRoleByName(@Param("name") String name);
	
	
	   public Role findByName(String name);
	   
	   public List<Role> findAll();
	
	
}
