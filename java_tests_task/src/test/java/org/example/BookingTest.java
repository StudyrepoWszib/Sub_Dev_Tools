package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private Client client;
    private Booking booking;
    @BeforeEach
    void setUp() {
        client = new Client("888888888", "David Carp" );
        LocalDate today = LocalDate.of(2024, 10, 10);
        booking = new Booking(client, "Krakow", "Madrid",today, 65863 );
    }

    @Test
    void setData_priwious() {
        //given
        LocalDate prewiousDay = LocalDate.of(2024,10,4);
        //when
        boolean result = booking.setData(prewiousDay);
        //then
        assertFalse(result);
    }

    @Test
    void setData_nextDay() {
        //given
        LocalDate prewiousDay = LocalDate.of(2024,11,25);
        //when
        boolean result = booking.setData(prewiousDay);
        //then
        assertTrue(result);
    }

    @Test
    void setData_nextYear() {
        //given
        LocalDate prewiousDay = LocalDate.of(2025,11,25);
        //when
        boolean result = booking.setData(prewiousDay);
        //then
        assertFalse(result);
    }

}