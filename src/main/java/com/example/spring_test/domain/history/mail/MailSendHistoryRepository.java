package com.example.spring_test.domain.history.mail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendHistoryRepository extends JpaRepository<MailSendHistory, Long> {

}
