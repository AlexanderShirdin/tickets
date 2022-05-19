package ru.tickets.HW;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws ParseException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TicketOffice ticketOffice = objectMapper.readValue(new File("tickets.json"), TicketOffice.class);

            printParsedObject(ticketOffice);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printParsedObject(TicketOffice ticketOffice) throws ParseException {
        printTicket(ticketOffice.getTickets());
    }

    private static long percentile(List<Long> latencies, double percentile) {

        int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
        return latencies.get(index - 1);
    }

    private static void printTicket(ArrayList<Ticket> tickets) throws ParseException {
        ArrayList<Long> latencies = new ArrayList<>();

        Long averTime = Long.valueOf(0);
        Long summTime = Long.valueOf(0);
        for (Ticket ticket : tickets) {
            printTicket(ticket);

            String departure = ticket.getDeparture_time();
            String arrival = ticket.getArrival_time();

            String time1 = departure;
            String time2 = arrival;

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            long difference = date2.getTime() - date1.getTime();
            Date date3 = new Date(difference);
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(date3);
            System.out.println("\tFlight time               : " + dateFormatted);

            Long time = (difference);
            summTime = summTime + time;
            latencies.add(difference);
        }
        Collections.sort(latencies);
        averTime = (summTime / tickets.size());

        System.out.println();
        Date date3 = new Date(averTime);
        Date date4 = new Date(percentile(latencies, 90));
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date3);
        String dateFormatted2 = formatter.format(date4);
        System.out.println("\tAverage flight time       : " + dateFormatted);
        System.out.println("\tPercentile 90             : " + dateFormatted2);
    }

    private static void printTicket(Ticket tickets) {
        System.out.println();
        System.out.println("\tTicket:");
        System.out.println("\tOrigin                    : " + tickets.getOrigin());
        System.out.println("\tOrigin name               : " + tickets.getOrigin_name());
        System.out.println("\tDestination               : " + tickets.getDestination());
        System.out.println("\tDestination name          : " + tickets.getDestination_name());
        System.out.println("\tDeparture date            : " + tickets.getDeparture_date());
        System.out.println("\tDeparture time            : " + tickets.getDeparture_time());
        System.out.println("\tArrival date              : " + tickets.getArrival_date());
        System.out.println("\tArrival time              : " + tickets.getArrival_time());
        System.out.println("\tCarrier                   : " + tickets.getCarrier());
        System.out.println("\tStops                     : " + tickets.getStops());
        System.out.println("\tPrice                     : " + tickets.getPrice());
    }
}