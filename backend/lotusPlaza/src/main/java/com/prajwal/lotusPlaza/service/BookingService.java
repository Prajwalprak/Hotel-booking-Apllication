package com.prajwal.lotusPlaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prajwal.lotusPlaza.exception.InvalidBookingRequestException;
import com.prajwal.lotusPlaza.exception.ResourceNotFoundException;
import com.prajwal.lotusPlaza.model.BookedRoom;
import com.prajwal.lotusPlaza.model.Room;
import com.prajwal.lotusPlaza.repository.BookingRepository;

@Service
public class BookingService implements IBookingService{
	
	private final BookingRepository repository;
	private final IRoomService roomService;
	
	@Autowired
	public BookingService(BookingRepository repository,IRoomService roomService) {
		this.repository = repository;
		this .roomService = roomService;
	}

	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		return repository.findByRoomId(roomId);
	}

	@Override
	public List<BookedRoom> getAllBookings() {
		return repository.findAll();
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            repository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return repository.findByBookingConfirmationCode(confirmationCode)
				.orElseThrow(()-> new ResourceNotFoundException("No booking found with this booking code: "+confirmationCode));
	}

	@Override
	public void cancelBooking(Long bookingId) {
		repository.deleteById(bookingId);
	}
	
	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

	
	@Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return repository.findByGuestEmail(email);
    }

}
