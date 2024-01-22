package com.example.spring_test.api.service.mail;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spring_test.client.mail.MailSendClient;
import com.example.spring_test.domain.history.mail.MailSendHistory;
import com.example.spring_test.domain.history.mail.MailSendHistoryRepository;

@ExtendWith(MockitoExtension.class) // Mock 만들거야
public class MailServiceTest {

	@Mock
	private MailSendClient mailSendClient;

	@Mock
	private MailSendHistoryRepository mailSendHistoryRepository;

	@InjectMocks
	private MailService mailService;

	@DisplayName("메일 전송 테스트")
	@Test
	void sendMail() {
		// given
		given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
				.willReturn(true);

		// when
		boolean result = mailService.sendMail("", "", "", "");

		// then
		assertThat(result).isTrue();
		verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
	}
}
