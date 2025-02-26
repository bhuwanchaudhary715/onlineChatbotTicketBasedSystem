package com.museum.onlineChatbotTicketBasedSystem.repository;

import com.museum.onlineChatbotTicketBasedSystem.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>{

    Orders findByRazorpayOrderId(String razorpayId);

}