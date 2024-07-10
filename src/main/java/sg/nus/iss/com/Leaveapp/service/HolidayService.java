package sg.nus.iss.com.Leaveapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.nus.iss.com.Leaveapp.repository.HolidayRepository;
import sg.nus.iss.com.Leaveapp.model.Holiday;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HolidayService {
    @Autowired
    private HolidayRepository holidayRepository;

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public void addHoliday(Holiday holiday) {
        holidayRepository.save(holiday);
    }

    public void updateHoliday(Long id, Holiday holiday) {
        Optional<Holiday> existingHoliday = holidayRepository.findById(id);
        if (existingHoliday.isPresent()) {
            Holiday updatedHoliday = existingHoliday.get();
            updatedHoliday.setName(holiday.getName());
            updatedHoliday.setDate(holiday.getDate());
            holidayRepository.save(updatedHoliday);
        }
    }

    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    public List<String> getFormattedHolidays() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, EEEE");
        return holidayRepository.findAll()
        		.stream().map(holiday -> String.format("%s\t%s", dateFormat.format(holiday.getDate()), holiday.getName()))
                
                .collect(Collectors.toList());
    }
    
    
    
    
    public List<Holiday> getHolidaysByYear(int year) {
        return holidayRepository.findAll().stream()
                .filter(holiday -> holiday.getDate().getYear() == year)
                .collect(Collectors.toList());
    }
    
    
    
    public List<Integer> getAvailableYears() {
        return holidayRepository.findAll().stream()
                .map(holiday -> holiday.getDate().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public void deleteHoliday1(Long id) {
        holidayRepository.deleteById(id);
    }
	
	
	
	
}
