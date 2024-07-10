package com.example.holidaycalendar.model;



import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.annotation.Nonnull;

//import java.time.LocalDate 

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "public_holidays")

public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;

    public Holiday() {}

    public Holiday(String name, Date date) {
        this.name = name;
        this.date = date;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

   
}