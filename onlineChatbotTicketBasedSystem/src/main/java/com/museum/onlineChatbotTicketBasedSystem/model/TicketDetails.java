package com.museum.onlineChatbotTicketBasedSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "ticket_details")
public class TicketDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "adult_tickets")
    private Integer adultticket;

    @Column(name = "child_tickets")
    private Integer childticket;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;


}