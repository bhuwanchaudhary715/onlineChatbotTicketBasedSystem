package com.museum.onlineChatbotTicketBasedSystem.controller;

import com.museum.onlineChatbotTicketBasedSystem.model.Orders;
import com.museum.onlineChatbotTicketBasedSystem.service.OrderService;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @PostMapping(value = "/createOrder", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Orders> createOrder(@RequestBody Orders orders) throws RazorpayException{
        Orders razorpayOrder = orderService.createOrder(orders);
        return new ResponseEntity<>(razorpayOrder,HttpStatus.CREATED);
    }


    @PostMapping("/paymentCallback")
    @ResponseBody
    public String paymentCallback(@RequestParam Map<String, String> response) {
        Orders updatedOrder = orderService.updateStatus(response);

        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <meta http-equiv=\"refresh\" content=\"5;url=/ticketGenerator.html\">" + // Auto-redirect
                "    <title>Payment Success</title>" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap\" rel=\"stylesheet\">" +
                "    <style>" +
                "        :root { --primary: #4ade80; --secondary: #22d3ee; }" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { " +
                "            min-height: 100vh; " +
                "            background: linear-gradient(135deg, #1a1a1a, #2d2d2d); " +
                "            font-family: 'Poppins', sans-serif; " +
                "            overflow: hidden; " +
                "        }" +
                "        .celebration { " +
                "            position: fixed; " +
                "            width: 100vw; height: 100vh; " +
                "            pointer-events: none; z-index: 0; " +
                "        }" +
                "        .confetti { " +
                "            position: absolute; " +
                "            width: 12px; height: 12px; " +
                "            animation: confetti 3s linear infinite; " +
                "            opacity: 0.8; " +
                "        }" +
                "        @keyframes confetti { " +
                "            0% { transform: translateY(-100%) rotate(0deg); } " +
                "            100% { transform: translateY(100vh) rotate(720deg); } " +
                "        }" +
                "        #cnt { " +
                "            display: flex; " +
                "            justify-content: center; " +
                "            align-items: center; " +
                "            min-height: 100vh; " +
                "            padding: 2rem; " +
                "            position: relative; " +
                "        }" +
                "        .card { " +
                "            background: rgba(255, 255, 255, 0.1); " +
                "            backdrop-filter: blur(15px); " +
                "            border-radius: 20px; " +
                "            padding: 2.5rem; " +
                "            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1); " +
                "            border: 1px solid rgba(255, 255, 255, 0.1); " +
                "            position: relative; " +
                "            overflow: hidden; " +
                "            transition: transform 0.3s ease; " +
                "        }" +
                "        .card:hover { transform: translateY(-5px); }" +
                "        .checkmark { " +
                "            width: 120px; height: 120px; " +
                "            border-radius: 50%; " +
                "            display: flex; " +
                "            align-items: center; " +
                "            justify-content: center; " +
                "            background: linear-gradient(45deg, var(--primary), var(--secondary)); " +
                "            box-shadow: 0 0 40px rgba(74, 222, 128, 0.3); " +
                "            animation: checkmarkPop 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55); " +
                "        }" +
                "        @keyframes checkmarkPop { " +
                "            0% { transform: scale(0); opacity: 0; } " +
                "            80% { transform: scale(1.1); } " +
                "            100% { transform: scale(1); opacity: 1; } " +
                "        }" +
                "        .detail-item { " +
                "            background: rgba(255, 255, 255, 0.05); " +
                "            border-radius: 12px; " +
                "            padding: 1.2rem; " +
                "            margin-bottom: 1rem; " +
                "            transition: transform 0.3s ease; " +
                "        }" +
                "        .detail-item:hover { transform: translateX(10px); }" +
                "        .button { " +
                "            background: linear-gradient(45deg, var(--primary), var(--secondary)); " +
                "            padding: 1rem 2rem; " +
                "            border-radius: 50px; " +
                "            font-weight: 600; " +
                "            transition: all 0.3s ease; " +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "<script>"
                + "setTimeout(function() {"
                + "   window.location.href = '/ticketGenerator.html';"
                + "}, 5000);"
                + "</script>"
                + "</body></html>"+

                "    <div class=\"celebration\">" +
                "        <div class=\"confetti\" style=\"left:10%;background:#4ade80;animation-delay:0.2s\"></div>" +
                "        <div class=\"confetti\" style=\"left:20%;background:#22d3ee;animation-delay:0.4s\"></div>" +
                "        <div class=\"confetti\" style=\"left:30%;background:#f59e0b;animation-delay:0.6s\"></div>" +
                "        <div class=\"confetti\" style=\"left:40%;background:#4ade80;animation-delay:0.8s\"></div>" +
                "        <div class=\"confetti\" style=\"left:50%;background:#22d3ee;animation-delay:1.0s\"></div>" +
                "    </div>" +
                "    <div id=\"cnt\">" +
                "        <div class=\"card\">" +
                "            <div class=\"flex flex-col items-center space-y-6\">" +
                "                <div class=\"checkmark\">" +
                "                    <svg viewBox=\"0 0 52 52\" style=\"width:50px;height:50px;stroke:white;\">" +
                "                        <path fill=\"none\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"4\" d=\"M14.1 27.2l7.1 7.2 16.7-16.8\"/>" +
                "                    </svg>" +
                "                </div>" +
                "                <h1 class=\"text-4xl font-bold text-white mt-6 mb-2\">Payment Successful! 🎉</h1>" +
                "                <p class=\"text-gray-300 text-center mb-6\">Thank you for your purchase!</p>" +
                "                <div class=\"w-full space-y-4\">" +
                "                    <div class=\"detail-item\">" +
                "                        <div class=\"flex justify-between items-center\">" +
                "                            <span class=\"text-gray-300\">Ticket ID:</span>" +
                "                            <span class=\"font-semibold text-white\">" + updatedOrder.getOrderId() + "</span>" +
                "                        </div>" +
                "                    </div>" +
                "                    <div class=\"detail-item\">" +
                "                        <div class=\"flex justify-between items-center\">" +
                "                            <span class=\"text-gray-300\">Amount Paid:</span>" +
                "                            <span class=\"font-semibold text-white\">" + updatedOrder.getAmount() + "</span>" +
                "                        </div>" +
                "                    </div>" +
                "                    <div class=\"detail-item\">" +
                "                        <div class=\"flex justify-between items-center\">" +
                "                            <span class=\"text-gray-300\">Order ID:</span>" +
                "                            <span class=\"font-semibold text-white\">" + updatedOrder.getRazorpayOrderId() + "</span>" +
                "                        </div>" +
                "                    </div>" +
                "                    <div class=\"detail-item\">" +
                "                        <div class=\"flex justify-between items-center\">" +
                "                            <span class=\"text-gray-300\">Status:</span>" +
                "                            <span class=\"text-green-600 font-semibold\">" + updatedOrder.getOrderStatus() + "</span>" +
                "                        </div>" +
                "                    </div>" +
                "                </div>" +
                "                <a href=\"/ticketGenerator.html\" class=\"button text-white\">Generate Your Ticket →</a>" +
                "            </div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
       }
    }