package com.museum.onlineChatbotTicketBasedSystem.repository;


import com.museum.onlineChatbotTicketBasedSystem.model.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// PersonalInfoRepository.java
@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

}
