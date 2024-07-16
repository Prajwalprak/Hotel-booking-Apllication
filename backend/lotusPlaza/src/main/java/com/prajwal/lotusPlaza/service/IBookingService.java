package com.prajwal.lotusPlaza.service;

import java.util.List;

import com.prajwal.lotusPlaza.model.BookedRoom;

public interface IBookingService {

	List<BookedRoom> getAllBookings();

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	void cancelBooking(Long bookingId);

	List<BookedRoom> getBookingsByUserEmail(String email);

}
