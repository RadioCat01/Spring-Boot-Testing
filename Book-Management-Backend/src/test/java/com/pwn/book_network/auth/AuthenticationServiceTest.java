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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private roleRepository roleRepository;

    @Mock
    private userRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private tokenRepository tokenRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Captor
    private ArgumentCaptor<EmailTemplateName> templateCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShouldRegister() throws MessagingException {
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
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(User.class));

        //Capture the arguments passed to the 'sendEmail' method to assert their value,
        //otherwise there is an error saying there's a difference between arguments
        verify(emailService).sendEmail(stringCaptor.capture(), stringCaptor.capture(),
                templateCaptor.capture(), stringCaptor.capture(),
                stringCaptor.capture(), stringCaptor.capture());

    }

    @Test
    void shouldThrowException_WhenRoleDoesNotExist(){

        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("NewPassword")
                .build();

        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> authenticationService.register(request));
    }


    @Test
    void shouldReturnJWTToken_WhenAuthentication_successful(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("firstName", "lastName");
        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email")
                .password("NewPassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(any(), any(User.class))).thenReturn("token");

        //When
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        //Then
        assertEquals("token", authenticationResponse.getToken());

        //Verify is called once,(not) not called, or called with incorrect arguments
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        //Verify is called once,(not) not called, or called with incorrect arguments
        verify(jwtService).generateToken(any(HashMap.class),eq(user));
    }

    @Test
    void shouldThrowException_WhenAuthentication_failed(){
        //Given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("wrongEmail", "wrongPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UsernameNotFoundException("Invalid Credentials"));

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(authenticationRequest));
    }


    @Test
    void shouldActivateAccount_whenTokenIsValid() throws MessagingException {
        //Given
        String tokenValue = "token";  //test token value
        Token token = Token.builder()
                .token(tokenValue)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(20))
                .user(User.builder()
                        .id(1)
                        .firstName("firstName")
                        .lastName("lastName")
                        .email("email")
                        .password("NewPassword")
                        .enabled(false)
                        .build())
                .build();   // test token

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));
        when(userRepository.findById(1)).thenReturn(Optional.of(token.getUser()));

        //When
        authenticationService.activateAccount(tokenValue);

        //Then
        assertTrue(token.getUser().isEnabled());

    }

    @Test
    void shouldSendNewToken_WhenExpiredToken() throws MessagingException {
        String tokenValue = "token";
        Token token = Token.builder()
                .token(tokenValue)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().minusMinutes(1)) //Make sure it's expired
                .user(User.builder()
                        .id(1)
                        .firstName("firstName")
                        .lastName("lastName")
                        .email("email")
                        .password("NewPassword")
                        .enabled(false)
                        .build())
                .build();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        //When
        RuntimeException exception = assertThrows(RuntimeException.class, ()->authenticationService.activateAccount(tokenValue));

        //Then
        assertEquals("Activation token is expired New one sent", exception.getMessage());
        verify(emailService).sendEmail(stringCaptor.capture(), stringCaptor.capture(),
                templateCaptor.capture(), stringCaptor.capture(),
                stringCaptor.capture(), stringCaptor.capture());
        verify(tokenRepository, times(1)).save(any()); //make sure it saves the new token
        verify(userRepository, never()).save(any()); //make sure never changes the user rope to enable access

    }

    @Test
    void shouldThrowException_WhenInvalidToken() throws MessagingException {
        //Given
        String tokenValue = "token";
        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        //Then
        assertThrows(RuntimeException.class, ()->authenticationService.activateAccount(tokenValue));
    }

    @Test
    void shouldGenerateAndSendActivationCode_6Digits(){

        String activationCode = authenticationService.generateAndSaveActivationCode(6);

        //Assert
        assertNotNull(activationCode);
        assertEquals(6, activationCode.length());
    }


    @Test
    void shouldGenerateAndSaveActivationCode_6Digits(){
        User user = User.builder()
                .id(1)
                .email("anyEmail.com")
                .build();

        String activationCode =(String) authenticationService.generateAndSaveActivationToken(user);

        assertNotNull(activationCode);
        verify(tokenRepository,times(1)).save(any(Token.class));
    }


    @Test
    void shouldSuccessfullyActivateAccount_whenTokenIsValid() throws MessagingException {

       User user = User.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .enabled(false)
                .build();

       Token token = Token.builder()
                .token("123456")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

       when(tokenRepository.findByToken("123456")).thenReturn(Optional.of(token));
       when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

       //When
        authenticationService.activateAccount("123456");

        //Then
        assertTrue(token.getUser().isEnabled());
        assertNotNull(token.getValidatedAt());
        verify(tokenRepository,times(1)).save(token);
        verify(userRepository,times(1)).save(user);


    }



























}