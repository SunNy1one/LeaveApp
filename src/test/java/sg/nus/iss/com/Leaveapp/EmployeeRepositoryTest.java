package sg.nus.iss.com.Leaveapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.com.Leaveapp.model.Employee;
import sg.nus.iss.com.Leaveapp.model.Role;
import sg.nus.iss.com.Leaveapp.repository.EmployeeRepository;
import sg.nus.iss.com.Leaveapp.repository.RoleRepository;

@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateAndFindEmployee() {
        // Create a new role
        Role role = new Role();
        role.setName("Manager");
        role = roleRepository.save(role);

        // Create a new employee
        Employee employee = new Employee("john.doe", "password", "John Doe", role);

        // Save the employee
        employee = employeeRepository.save(employee);

        // Verify the employee was saved
        assertThat(employee.getId()).isNotNull();

        // Retrieve the employee by ID
        Employee foundEmployee = employeeRepository.findById(employee.getId()).orElse(null);

        // Verify the employee was retrieved
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getUsername()).isEqualTo("john.doe");
        assertThat(foundEmployee.getName()).isEqualTo("John Doe");

        // Compare role properties instead of object reference
        assertThat(foundEmployee.getRole().getName()).isEqualTo(role.getName());
    }

    @Test
    public void testEmployeeWithManager() {
        // Create roles
        Role managerRole = new Role();
        managerRole.setName("Manager");
        managerRole = roleRepository.save(managerRole);

        Role employeeRole = new Role();
        employeeRole.setName("Employee");
        employeeRole = roleRepository.save(employeeRole);

        // Create a manager
        Employee manager = new Employee("manager", "password", "Manager Name", managerRole);
        manager = employeeRepository.save(manager);

        // Create an employee with a manager
        Employee employee = new Employee("john.doe", "password", "John Doe", employeeRole);
        employee.setManager(manager);
        employee = employeeRepository.save(employee);

        // Verify the employee was saved with a manager
        assertThat(employee.getId()).isNotNull();
        assertThat(employee.getManager()).isNotNull();
        assertThat(employee.getManager().getUsername()).isEqualTo("manager");

        // Retrieve the employee by ID and verify the manager
        Employee foundEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getManager()).isNotNull();
        assertThat(foundEmployee.getManager().getUsername()).isEqualTo("manager");
    }

    @Test
    @Transactional
    public void testEmployeeWithReportees() {
        // Create a role
        Role role = new Role();
        role.setName("Employee");
        role = roleRepository.save(role);

        // Create a manager
        Employee manager = new Employee("manager", "password", "Manager Name", role);
        manager = employeeRepository.save(manager);

        // Ensure the manager is saved and has an ID
        assertThat(manager.getId()).isNotNull();

        // Create employees who report to the manager
        Employee employee1 = new Employee("john.doe1", "password", "John Doe 1", role);
        employee1.setManager(manager);
        employee1 = employeeRepository.save(employee1);

        Employee employee2 = new Employee("john.doe2", "password", "John Doe 2", role);
        employee2.setManager(manager);
        employee2 = employeeRepository.save(employee2);

        // Retrieve the manager and verify the reportees
        Employee foundManager = employeeRepository.findById(manager.getId()).orElse(null);

        // Verify that the manager was retrieved
        assertThat(foundManager).isNotNull();

        // Ensure that the reportees list is loaded
        assertThat(foundManager.getReportees()).isNotNull();
        assertThat(foundManager.getReportees()).hasSize(2);

        // Verify the reportees
        assertThat(foundManager.getReportees().stream().map(Employee::getUsername))
            .containsExactlyInAnyOrder("john.doe1", "john.doe2");
    }
}
