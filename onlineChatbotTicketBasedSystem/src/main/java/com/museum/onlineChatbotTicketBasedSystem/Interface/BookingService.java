package com.museum.onlineChatbotTicketBasedSystem.Interface;

import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.model.TicketDetails;


public interface BookingService {

    boolean savePersonalInfo(PersonalInfo info);

    boolean saveTicketDetails(TicketDetails details);

    TicketDetails getTicketById(Integer id);

    boolean updateTicketDetails(TicketDetails ticket);

    boolean deleteTicketById(Long id);
}