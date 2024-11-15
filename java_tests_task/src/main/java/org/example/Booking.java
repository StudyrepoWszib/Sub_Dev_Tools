package org.example;

import java.time.LocalDate;


public class Booking {

    Client client;
    String miejsce_wylotu;
    String miejsce_przylotu;
    LocalDate data;
    int numer_lotu;

    public Booking(Client client, String miejsce_wylotu, String miejsce_przylotu, LocalDate data, int numer_lotu) {
        this.client = client;
        this.miejsce_wylotu = miejsce_wylotu;
        this.miejsce_przylotu = miejsce_przylotu;
        this.data = data;
        this.numer_lotu = numer_lotu;
    }

    public boolean setData(LocalDate data) {
        java.time.LocalDate this_date = java.time.LocalDate.now();
        java.time.LocalDate date_2024 = java.time.LocalDate.of(2024,12,31);
        if(data.isAfter(this_date) && data.isBefore(date_2024)){
            this.data = data;
            return true;
        }
        else return false;
    }
}
