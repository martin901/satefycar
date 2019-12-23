package com.example.demo.servicetests;

import com.example.demo.builders.*;
import com.example.demo.models.*;
import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.Enums.UserRole;
import com.example.demo.repositories.PolicyRequestRepository;
import com.example.demo.services.PolicyRequestServiceImpl;
import com.example.demo.services.UserService;
import com.example.demo.services.VehicleRegistrationImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PolicyRequestServiceTests {
    @Mock
    private PolicyRequestRepository policyRequestRepository;
    @Mock
    private VehicleRegistrationImageService vehicleRegistrationImageService;
    @Mock
    private UserService userService;
    @InjectMocks
    private PolicyRequestServiceImpl policyRequestService;

    private CarBrand carBrand = new CarBrandBuilder()
            .withBrandName("Audi")
            .build();
    private CarModel carModel = new CarModelBuilder()
            .withCarBrand(carBrand)
            .withModelName("A4")
            .build();
    private Offer offer = new OfferBuilder()
            .withCarModel(carModel)
            .withCubicCapacity(1500)
            .withFirstRegDate(LocalDate.of(2002, Month.APRIL, 18))
            .withDriverAge(27)
            .withHadAccident(true)
            .build();
    private Name name = new NameBuilder()
            .withFirstName("Ivan")
            .withLastName("Petrov")
            .build();
    private Phone phone = new PhoneBuilder()
            .withPhone("0884050360")
            .build();
    private Address address = new AddressBuilder()
            .withAddress("Sofia")
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
    private PolicyRequest policyRequest = new PolicyRequestBuilder()
            .withOffer(offer)
            .withEffectiveDate(LocalDate.now())
            .withUser(user)
            .withRequestDate(Date.from(LocalDate.of(2019, Month.JULY, 28).atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .build();
    private PolicyRequest policyRequest1 = new PolicyRequestBuilder()
            .withOffer(offer)
            .withEffectiveDate(LocalDate.of(2020, Month.JANUARY, 1))
            .withUser(user)
            .withRequestDate(Date.from(LocalDate.of(2019, Month.JULY, 29).atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .build();
    private Date beforeDate = Date.from(LocalDate.of(2019, Month.JULY, 27).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private Date afterDate = Date.from(LocalDate.of(2019, Month.JULY, 30).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private Date incrementedDate = Date.from(LocalDate.of(2019, Month.JULY, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private Date incrementedTodayDate = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private Date firstDate = new Date(Long.MIN_VALUE);
    private Pageable pageable = PageRequest.of(0, 8);


    @Test
    public void getAll_ShouldReturnAllPolicyRequests() {
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAll(pageable)).thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.getAll(pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getById_ShouldReturnElementWithSuchIdIfExist() {
        PolicyRequest expectedResult = policyRequest;
        when(policyRequestRepository.findById(policyRequest.getId())).thenReturn(Optional.of(expectedResult));
        PolicyRequest actualResult = policyRequestService.getById(policyRequest.getId());
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getById_ShouldThrowExceptionWhenPolicyRequestWithIdNotExist() {
        when(policyRequestRepository.findById(1L)).thenReturn(Optional.empty());
        policyRequestService.getById(1L);
    }

    @Test
    public void create_ShouldCreateNewPolicyRequest() {
        PolicyRequest expectedResult = policyRequest;
        MockMultipartFile img = new MockMultipartFile("img", "", "image/jpeg", "some image".getBytes());
        VehicleRegistrationImage image = new VehicleRegistrationImage();
        try {
            image.setImage(img.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Mockito.when(vehicleRegistrationImageService.saveImage(img)).thenReturn(image);
        when(policyRequestRepository.save(policyRequest)).thenReturn(expectedResult);
        PolicyRequest actualResult = policyRequestService.create(policyRequest, img);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByUserAndRequestDateAndStatus_WhenUserAndRequestDateFromAndRequestDateToAndStatusArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, beforeDate, incrementedDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), beforeDate, afterDate, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByUserAndRequestDateAndStatus_WhenUserAndRequestDateFromAndRequestDateToArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, beforeDate, incrementedDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), beforeDate, afterDate, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByUserAndRequestDateAndStatus_WhenUserAndRequestDateFromAndStatusArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, beforeDate, incrementedTodayDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), beforeDate, null, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByUserAndRequestDateAndStatus_WhenUserAndRequestDateToAndStatusArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, firstDate, incrementedDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), null, afterDate, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByRequestStatusAndRequestDate_WhenRequestDateFromAndRequestDateToAndStatusArePassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(beforeDate, incrementedDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, beforeDate, afterDate, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllPendingByUserAndRequestDate_WhenUserAndRequestDateFromArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, beforeDate, incrementedTodayDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), beforeDate, null, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllPendingByUserAndRequestDate_WhenUserAndRequestDateToArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, firstDate, incrementedDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), null, afterDate, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByUserAndRequestStatus_WhenUserAndStatusArePassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByUserAndRequestStatusOrderByRequestDateDesc(user, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), null, null, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByRequestStatusAndRequestDate_WhenRequestDateFromAndRequestDateToArePassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(beforeDate, incrementedDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, beforeDate, afterDate, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByRequestStatusAndRequestDate_WhenRequestDateFromAndStatusArePassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(beforeDate, incrementedTodayDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, beforeDate, null, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllByRequestStatusAndRequestDate_WhenRequestDateToAndStatusArePassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository
                .findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(firstDate, incrementedDate, RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, null, afterDate, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllPendingByUser_WhenUserIsPassed(){
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByUserAndRequestStatusOrderByRequestDateDesc(user, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(user.getUsername(), null, null, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllPendingByReqDate_WhenRequestDateFromIsPassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(beforeDate, incrementedTodayDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, beforeDate, null, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetAllPendingByReqDate_WhenRequestDateToIsPassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(firstDate, incrementedDate, RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, null, afterDate, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetByRequestStatus_WhenStatusIsPassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByRequestStatusOrderByRequestDateDesc(RequestStatus.APPROVED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, null, null, RequestStatus.APPROVED, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void get_ShouldCallGetByRequestStatus_WhenNothingIsPassed(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByRequestStatusOrderByRequestDateDesc(RequestStatus.PENDING, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.get(null, null, null, null, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getAllPendingByUser_ShouldReturnAllPendingPolicyRequestsFromSaidUser() {
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByUserAndRequestStatusOrderByRequestDateDesc(user, RequestStatus.PENDING, pageable)).thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.getAllPendingByUser(user, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getAllByUser_ShouldReturnAllPolicyRequestsWhichAreNotCanceledFromSaidUser(){
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByUserAndRequestStatusIsNotOrderByRequestDateDesc(user, RequestStatus.CANCELED, pageable))
                .thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.getAllByUser(user, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void getAllSortedByReqDate_ShouldReturnAllPolicyRequestsSortedByDateOfRequest() {
        policyRequest.setRequestDate(new Date(2019, Calendar.JULY, 29));
        policyRequest1.setRequestDate(new Date(2019, Calendar.JULY, 28));
        List<PolicyRequest> expectedResult = Arrays.asList(policyRequest1, policyRequest);
        when(policyRequestRepository.findAll(new Sort(Sort.Direction.DESC, "requestDate"))).thenReturn(expectedResult);
        List<PolicyRequest> actualResult = policyRequestService.getAllSortedByReqDate().getContent();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void approveRequestById_ShouldChangeRequestStatusToApproved() {
        PolicyRequest expectedResult = policyRequest;
        when(policyRequestRepository.findById(policyRequest.getId())).thenReturn(Optional.of(expectedResult));
        policyRequestService.approveRequestById(policyRequest.getId(), user);
        verify(policyRequestRepository, times(1)).save(expectedResult);
        assertThat(expectedResult.getRequestStatus()).isEqualTo(RequestStatus.APPROVED);
    }

    @Test
    public void declineRequestById_ShouldChangeRequestStatusToDeclined() {
        PolicyRequest expectedResult = policyRequest;
        when(policyRequestRepository.findById(policyRequest.getId())).thenReturn(Optional.of(expectedResult));
        policyRequestService.declineRequestById(policyRequest.getId(), user);
        verify(policyRequestRepository, times(1)).save(expectedResult);
        assertThat(expectedResult.getRequestStatus()).isEqualTo(RequestStatus.DECLINED);
    }

    @Test
    public void getAllByRequestStatus_ShouldReturnAllRequestsWithSaidStatus() {
        policyRequest.setRequestStatus(RequestStatus.PENDING);
        policyRequest1.setRequestStatus(RequestStatus.PENDING);
        Page<PolicyRequest> expectedResult = new PageImpl<>(Arrays.asList(policyRequest, policyRequest1));
        when(policyRequestRepository.findAllByRequestStatusOrderByRequestDateDesc(RequestStatus.PENDING, pageable)).thenReturn(expectedResult);
        Page<PolicyRequest> actualResult = policyRequestService.getAllByRequestStatus(RequestStatus.PENDING, pageable);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void cancelRequestById_ShouldChangeRequestStatusOfEntityWithSuchIdToCancelled(){
        PolicyRequest expectedResult = policyRequest;
        when(policyRequestRepository.findById(policyRequest.getId())).thenReturn(Optional.of(expectedResult));
        policyRequestService.cancelRequestById(policyRequest.getId());
        verify(policyRequestRepository, times(1)).save(expectedResult);
    }
}
