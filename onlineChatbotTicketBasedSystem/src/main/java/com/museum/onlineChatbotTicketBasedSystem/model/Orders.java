package com.museum.onlineChatbotTicketBasedSystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "paymentorder")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long orderId;
    private String name;
    private String email;
    private Integer amount;
    private String orderStatus;
    private String razorpayOrderId;


}