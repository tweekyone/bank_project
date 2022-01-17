package com.epam.bank.operatorinterface.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.operatorinterface.entity.AccountPlan;
import com.epam.bank.operatorinterface.entity.User;
import com.epam.bank.operatorinterface.repository.UserRepository;
import com.epam.bank.operatorinterface.service.dto.CreateUserDto;
import com.epam.bank.operatorinterface.util.ValidationFacade;
import com.epam.bank.operatorinterface.util.validator.EmailIsUnique;
import com.epam.bank.operatorinterface.util.validator.UsernameIsUnique;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.ReflectionUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private ValidationFacade validationFacadeMock;

    @Mock
    private AccountService accountServiceMock;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<CreateUserDto> createUserDtoCaptor;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldCreateUser() {
        var name = RandomStringUtils.randomAlphabetic(6);
        var surname = RandomStringUtils.randomAlphabetic(6);
        var phone = RandomStringUtils.randomAlphabetic(6);
        var username = RandomStringUtils.randomAlphabetic(6);
        var email = RandomStringUtils.randomAlphabetic(6).concat("@gmail.com");
        var rawPassword = RandomStringUtils.randomAlphabetic(6);

        userService.create(name, surname, phone, username, email, rawPassword);

        verify(validationFacadeMock).validateOrThrow(createUserDtoCaptor.capture());
        Assertions.assertThat(createUserDtoCaptor.getValue().getName()).isEqualTo(name);
        Assertions.assertThat(createUserDtoCaptor.getValue().getSurname()).isEqualTo(surname);
        Assertions.assertThat(createUserDtoCaptor.getValue().getPhone()).isEqualTo(phone);
        Assertions.assertThat(createUserDtoCaptor.getValue().getUsername()).isEqualTo(username);
        Assertions.assertThat(createUserDtoCaptor.getValue().getEmail()).isEqualTo(email);
        Assertions.assertThat(createUserDtoCaptor.getValue().getRawPassword()).isEqualTo(rawPassword);

        verify(userRepositoryMock).save(userCaptor.capture());
        Assertions.assertThat(userCaptor.getValue().getName()).isEqualTo(name);
        Assertions.assertThat(userCaptor.getValue().getSurname()).isEqualTo(surname);
        Assertions.assertThat(userCaptor.getValue().getPhoneNumber()).isEqualTo(phone);
        Assertions.assertThat(userCaptor.getValue().getUsername()).isEqualTo(username);
        Assertions.assertThat(userCaptor.getValue().getEmail()).isEqualTo(email);
        // ToDo: assert hash og password when spring security is ready
        Assertions.assertThat(userCaptor.getValue().getPassword()).isEqualTo(rawPassword);
        Assertions.assertThat(userCaptor.getValue().isEnabled()).isTrue();
        Assertions.assertThat(userCaptor.getValue().getFailedLoginAttempts()).isEqualTo(0);
    }

    @Test
    public void dtoShouldHaveParticularValidationAnnotations() {
        var fieldAnnotationsMap = Map.of(
            "name", new String[]{NotNull.class.getName(), NotBlank.class.getName(), Size.class.getName()},
            "surname", new String[]{NotNull.class.getName(), NotBlank.class.getName(), Size.class.getName()},
            "phone", new String[]{
                NotNull.class.getName(), NotBlank.class.getName(), Size.class.getName(), Pattern.class.getName()
            },
            "username", new String[]{
                NotNull.class.getName(),
                NotBlank.class.getName(), 
                Size.class.getName(),
                UsernameIsUnique.class.getName()
            },
            "email", new String[]{
                NotNull.class.getName(), NotBlank.class.getName(), Email.class.getName(), EmailIsUnique.class.getName()
            },
            "rawPassword", new String[]{NotNull.class.getName(), NotBlank.class.getName(), Size.class.getName()}
        );

        for (var entry : fieldAnnotationsMap.entrySet()) {
            var field = ReflectionUtils.findRequiredField(CreateUserDto.class, entry.getKey());
            var fieldAnnotations = Arrays.stream(field.getAnnotations())
                .map(annotation -> annotation.annotationType().getName())
                .collect(Collectors.toList());
            Assertions.assertThat(fieldAnnotations).containsExactlyInAnyOrder(entry.getValue());
        }

        var nameSizeAnnotation = getAnnotation(CreateUserDto.class, "name", Size.class);
        Assertions.assertThat(nameSizeAnnotation.min()).isEqualTo(2);
        Assertions.assertThat(nameSizeAnnotation.max()).isEqualTo(255);

        var surnameSizeAnnotation = getAnnotation(CreateUserDto.class, "surname", Size.class);
        Assertions.assertThat(surnameSizeAnnotation.min()).isEqualTo(2);
        Assertions.assertThat(surnameSizeAnnotation.max()).isEqualTo(255);

        var phonePatternAnnotation = getAnnotation(CreateUserDto.class, "phone", Pattern.class);
        Assertions.assertThat(phonePatternAnnotation.regexp()).isEqualTo("[+()\\-0-9]+");

        var phoneSizeAnnotation = getAnnotation(CreateUserDto.class, "phone", Size.class);
        Assertions.assertThat(phoneSizeAnnotation.min()).isEqualTo(5);
        Assertions.assertThat(phoneSizeAnnotation.max()).isEqualTo(20);

        var usernameSizeAnnotation = getAnnotation(CreateUserDto.class, "username", Size.class);
        Assertions.assertThat(usernameSizeAnnotation.min()).isEqualTo(4);
        Assertions.assertThat(usernameSizeAnnotation.max()).isEqualTo(255);

        var rawPasswordSizeAnnotation = getAnnotation(CreateUserDto.class, "rawPassword", Size.class);
        Assertions.assertThat(rawPasswordSizeAnnotation.min()).isEqualTo(8);
        Assertions.assertThat(rawPasswordSizeAnnotation.max()).isEqualTo(255);
    }

    @Test
    public void shouldCreateClientUser() {
        var name = RandomStringUtils.randomAlphabetic(6);
        var surname = RandomStringUtils.randomAlphabetic(6);
        var phone = RandomStringUtils.randomAlphabetic(6);
        var username = RandomStringUtils.randomAlphabetic(6);
        var email = RandomStringUtils.randomAlphabetic(6).concat("@gmail.com");
        var rawPassword = RandomStringUtils.randomAlphabetic(6);

        when(userRepositoryMock.save(any(User.class))).then(invocation -> {
            var user = (User) invocation.getArgument(0);
            user.setId(RandomUtils.nextLong());
            return user;
        });

        var user = userService.createClient(name, surname, phone, username, email, rawPassword);

        verify(accountServiceMock).create(user.getId(), AccountPlan.BASE);
        Assertions.assertThat(user.getName()).isEqualTo(name);
        Assertions.assertThat(user.getSurname()).isEqualTo(surname);
        Assertions.assertThat(user.getPhoneNumber()).isEqualTo(phone);
        Assertions.assertThat(user.getUsername()).isEqualTo(username);
        Assertions.assertThat(user.getEmail()).isEqualTo(email);
        // ToDo: assert hash og password when spring security is ready
        Assertions.assertThat(user.getPassword()).isEqualTo(rawPassword);
        Assertions.assertThat(user.isEnabled()).isTrue();
        Assertions.assertThat(user.getFailedLoginAttempts()).isEqualTo(0);
    }

    private <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        return ReflectionUtils.findRequiredField(clazz, fieldName).getAnnotation(annotationClass);
    }
}
