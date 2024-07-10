package sg.nus.iss.com.Leaveapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.nus.iss.com.Leaveapp.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
