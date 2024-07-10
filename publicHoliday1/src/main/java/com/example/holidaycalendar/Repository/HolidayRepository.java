package com.example.holidaycalendar.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.holidaycalendar.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
