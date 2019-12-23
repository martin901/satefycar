package com.example.demo.servicetests;

import com.example.demo.builders.AddressBuilder;
import com.example.demo.builders.NameBuilder;
import com.example.demo.builders.PhoneBuilder;
import com.example.demo.builders.UserBuilder;
import com.example.demo.models.Address;
import com.example.demo.models.Enums.UserRole;
import com.example.demo.models.Name;
import com.example.demo.models.Phone;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserServiceImpl;
import org.apache.poi.ss.formula.functions.Na;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Name name = new NameBuilder()
            .withFirstName("Ivan")
            .withLastName("Petrov")
            .build();
    private Name nameForUpdateTest = new NameBuilder()
            .withFirstName(null)
            .withLastName(null)
            .build();
    private Phone phone = new PhoneBuilder()
            .withPhone("0884050360")
            .build();
    private Phone phoneForUpdateTest = new PhoneBuilder()
            .withPhone(null)
            .build();
    private Address address = new AddressBuilder()
            .withAddress("Sofia")
            .build();
    private Address addressForUpdateTest = new AddressBuilder()
            .withAddress("Burgas")
            .build();
    private User user = new UserBuilder()
            .withName(name)
            .withEmail("ivan@abv.bg")
            .withPhone(phone)
            .withAddress(address)
            .withUsername("vankata98")
            .withPassword("parola123")
            .withRole(UserRole.BASICUSER)
            .build();
    private User user1 = new UserBuilder()
            .withName(name)
            .withEmail("vankata@abv.bg")
            .withPhone(phone)
            .withAddress(address)
            .withUsername("vankata98")
            .withPassword("password12")
            .withRole(UserRole.BASICUSER)
            .build();
    private User userForUpdateTest = new UserBuilder()
            .withName(nameForUpdateTest)
            .withEmail(null)
            .withPhone(phoneForUpdateTest)
            .withAddress(addressForUpdateTest)
            .withUsername(null)
            .withPassword(null)
            .withRole(null)
            .build();
    private Pageable pageable = PageRequest.of(0, 20);


    @Test
    public void create_ShouldCreateNewUser(){
        User expectedResult = user;
        when(userRepository.save(user)).thenReturn(expectedResult);
        User actualResult = userService.create(user, true);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getById_ShouldReturnUserWithSaidId() {
        User expectedResult = user;
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(expectedResult));
        User actualResult = userService.getById(user.getId());
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_ShouldThrowExceptionWhenIdNotExist() {
        when(userRepository.findById(user.getId())).thenThrow(IllegalArgumentException.class);
        userService.getById(user.getId());
    }

    @Test
    public void getAll_ShouldReturnAllUsers(){
        Page<User> expectedResult = new PageImpl<>(Arrays.asList(user, user1));
        when(userRepository.findAll(pageable)).thenReturn(expectedResult);
        Page<User> actualResult = userService.getAll(pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getByUsername_ShouldReturnUserWithSaidUsername(){
        User expectedResult = user;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(expectedResult);
        User actualResult = userService.getByUsername(user.getUsername());
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getByEmail_ShouldReturnUserWithSaidEmail(){
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        User result = userService.getByEmail(user.getEmail());
        assertThat(user).isEqualTo(result);
    }

    @Test
    public void updateById_ShouldUpdateUserWhenPropertyIsChanged() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(userForUpdateTest)).thenReturn(userForUpdateTest);
        User result = userService.updateById(user.getId(), userForUpdateTest);
        Assert.assertEquals("Burgas", result.getAddress().getAddress());
        Assert.assertEquals(user.getName().getFirstName(), result.getName().getFirstName());
        Assert.assertEquals(user.getName().getLastName(), result.getName().getLastName());
    }

    @Test
    public void loadUserByUsername_ShouldLoadUserWithProvidedUsernameIfExist() {
        User expectedResult = user;
        when(userRepository.findByUsername(user.getUsername())).thenReturn(expectedResult);
        UserDetails actualResult = userService.loadUserByUsername(user.getUsername());
        assertThat(actualResult.getUsername()).isEqualTo(expectedResult.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_ShouldThrowException_WhenUsernameNotExist() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        userService.loadUserByUsername(user.getUsername());
    }
}
