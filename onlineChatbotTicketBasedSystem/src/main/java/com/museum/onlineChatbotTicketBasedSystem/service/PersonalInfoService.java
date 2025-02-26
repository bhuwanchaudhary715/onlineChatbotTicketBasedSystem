package com.museum.onlineChatbotTicketBasedSystem.service;

import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import com.museum.onlineChatbotTicketBasedSystem.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

;


@Service
@RequiredArgsConstructor
public class PersonalInfoService {


    private final PersonalInfoRepository personalInfoRepository;


    public List<PersonalInfo> getAllPersonalInfos() {
        return personalInfoRepository.findAll();
    }

    public PersonalInfo getPersonalInfoById(Long id) {
        return personalInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonalInfo not found with id: " + id));
    }

}