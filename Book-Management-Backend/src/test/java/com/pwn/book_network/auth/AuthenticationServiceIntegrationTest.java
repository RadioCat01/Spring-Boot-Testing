package com.pwn.book_network.auth;

import com.pwn.book_network.email.EmailService;
import com.pwn.book_network.email.EmailTemplateName;
import com.pwn.book_network.role.Roles;
import com.pwn.book_network.role.roleRepository;
import com.pwn.book_network.security.JwtService;
import com.pwn.book_network.user.Token;
import com.pwn.book_network.user.User;
import com.pwn.book_network.user.tokenRepository;
import com.pwn.book_network.user.userRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private roleRepository roleRepository;

    @MockBean
    private userRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private tokenRepository tokenRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<EmailTemplateName> templateCaptor;

    @Test
    void shouldRegister() throws MessagingException {
        //Given
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("NewPassword")
                .build();

        Roles newRole = new Roles();
        newRole.setName("USER");

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(newRole));
        when(passwordEncoder.encode("NewPassword")).thenReturn("NewEncodedPassword");

        //When
        authenticationService.register(request);

        //Then
        verify(roleRepository,times(2)).findByName("USER");
        /*
        The roleRepository is called 2 times because this way the Spring context is up,
        so if the role repository is called anywhere else it is also asserted and test fails.
         */
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));

        // Capture and verify email sending
        verify(emailService).sendEmail(stringCaptor.capture(), stringCaptor.capture(),
                templateCaptor.capture(), stringCaptor.capture(),
                stringCaptor.capture(), stringCaptor.capture());
    }

}
