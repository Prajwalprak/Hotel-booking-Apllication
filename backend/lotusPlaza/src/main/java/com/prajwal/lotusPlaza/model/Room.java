package com.prajwal.lotusPlaza.model;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }
    
    public void addBooking(BookedRoom booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRoom(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }

	public Long getId() {
		return id;
	}

	public String getRoomType() {
		return roomType;
	}

	public BigDecimal getRoomPrice() {
		return roomPrice;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public Blob getPhoto() {
		return photo;
	}

	public List<BookedRoom> getBookings() {
		return bookings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public void setRoomPrice(BigDecimal roomPrice) {
		this.roomPrice = roomPrice;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

	public void setBookings(List<BookedRoom> bookings) {
		this.bookings = bookings;
	}
	
	
    
	
}
