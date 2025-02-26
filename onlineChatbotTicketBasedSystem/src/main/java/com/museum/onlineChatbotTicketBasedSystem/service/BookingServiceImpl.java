package com.museum.onlineChatbotTicketBasedSystem.service;

import com.museum.onlineChatbotTicketBasedSystem.Interface.BookingService;
import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.model.TicketDetails;
import com.museum.onlineChatbotTicketBasedSystem.repository.PersonalInfoRepository;
import com.museum.onlineChatbotTicketBasedSystem.repository.TicketDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BookingServiceImpl implements BookingService {

    private final PersonalInfoRepository personalInfoRepository;
    private final TicketDetailsRepository ticketDetailsRepository;

    @Autowired
    public BookingServiceImpl(
            PersonalInfoRepository personalInfoRepository,
            TicketDetailsRepository ticketDetailsRepository
    ) {
        this.personalInfoRepository = personalInfoRepository;
        this.ticketDetailsRepository = ticketDetailsRepository;
    }

    @Override
    public boolean savePersonalInfo(PersonalInfo info) {
        try {
            personalInfoRepository.save(info);
            return true;
        } catch (Exception e) {
            // Log error (e.g., using a logger)
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveTicketDetails(TicketDetails details) {
        try {
            ticketDetailsRepository.save(details);
            return true;
        } catch (Exception e) {
            // Log error
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public TicketDetails getTicketById(Integer id) {
        return ticketDetailsRepository.findById(id.longValue())
                .orElse(null);
    }

    @Override
    @Transactional
    public boolean updateTicketDetails(TicketDetails ticket) {
        try {
            ticketDetailsRepository.save(ticket);
            return true;
        } catch (Exception e) {
            // Log error
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteTicketById(Long id) {
        try {
            if(ticketDetailsRepository.existsById(id)) {
                ticketDetailsRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            // Log error
            return false;
        }
    }
}