package com.museum.onlineChatbotTicketBasedSystem.service;


import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.model.TicketDetails;
import com.museum.onlineChatbotTicketBasedSystem.repository.PersonalInfoRepository;
import com.museum.onlineChatbotTicketBasedSystem.repository.TicketDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class BookingService {

    private final PersonalInfoRepository personalInfoRepository;
    private final TicketDetailsRepository ticketDetailsRepository;


    @Autowired
    public BookingService(PersonalInfoRepository personalInfoRepository, TicketDetailsRepository ticketDetailsRepository) {
        this.personalInfoRepository = personalInfoRepository;
        this.ticketDetailsRepository = ticketDetailsRepository;
    }


    public PersonalInfo savePersonalInfo(PersonalInfo personalInfo) {
        return personalInfoRepository.save(personalInfo);
    }



    public TicketDetails saveTicketDetails(String email, TicketDetails ticketDetails) {
        return ticketDetailsRepository.save(ticketDetails);
    }
}