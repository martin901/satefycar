package com.example.demo.services;

import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;
import com.example.demo.models.VehicleRegistrationImage;
import com.example.demo.repositories.PolicyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@Transactional
@EnableScheduling
public class PolicyRequestServiceImpl implements PolicyRequestService {
    private PolicyRequestRepository policyRequestRepository;
    private VehicleRegistrationImageService vehicleRegistrationImageService;
    private UserService userService;

    @Autowired
    public PolicyRequestServiceImpl(PolicyRequestRepository policyRequestRepository, VehicleRegistrationImageService vehicleRegistrationImageService, UserService userService) {
        this.policyRequestRepository = policyRequestRepository;
        this.vehicleRegistrationImageService = vehicleRegistrationImageService;
        this.userService = userService;
    }

    @Override
    public Page<PolicyRequest> getAll(Pageable pageable) {
        return policyRequestRepository.findAll(pageable);
    }

    @Override
    public PolicyRequest getById(Long id) {
        if (!policyRequestRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException(String.format("Policy request with id %d doesn't exist", id));
        }
        return policyRequestRepository.findById(id).get();
    }

    @Override
    public PolicyRequest create(PolicyRequest policyRequest, MultipartFile img) {
        VehicleRegistrationImage registrationImage = vehicleRegistrationImageService.saveImage(img);
        policyRequest.setImage(registrationImage);
        return policyRequestRepository.save(policyRequest);
    }

    @Override
    public Page<PolicyRequest> get(String user, Date fromDate, Date toDate, RequestStatus status, Pageable pageable) {
        User actualUser = userService.getByUsername(user);
        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date firstDate = new Date(Long.MIN_VALUE);
        if (user != null && fromDate != null && toDate != null && status != null) {
            return getAllByUserAndRequestDateAndStatus(actualUser, fromDate, toDate, status, pageable);
        } else if (user != null && fromDate != null && toDate != null){
            return getAllByUserAndRequestDateAndStatus(actualUser, fromDate, toDate, RequestStatus.PENDING, pageable);
        } else if (user != null && fromDate != null && status != null){
            return getAllByUserAndRequestDateAndStatus(actualUser, fromDate, todayDate, status, pageable);
        } else if (user != null && toDate != null && status != null) {
            return getAllByUserAndRequestDateAndStatus(actualUser, firstDate, toDate, status, pageable);
        } else if (fromDate != null && toDate != null && status != null){
            return getAllByRequestStatusAndRequestDate(fromDate, toDate, status, pageable);
        } else if (user != null && fromDate != null) {
            return getAllPendingByUserAndRequestDate(actualUser, fromDate, todayDate, pageable);
        } else if (user != null && toDate != null) {
            return getAllPendingByUserAndRequestDate(actualUser, firstDate, toDate, pageable);
        } else if (user != null && status != null) {
            return getAllByUserAndRequestStatus(actualUser, status, pageable);
        } else if (fromDate != null && toDate != null){
            return getAllByRequestStatusAndRequestDate(fromDate, toDate, RequestStatus.PENDING, pageable);
        } else if (fromDate != null && status != null) {
            return getAllByRequestStatusAndRequestDate(fromDate, todayDate, status, pageable);
        } else if (toDate != null && status != null){
            return getAllByRequestStatusAndRequestDate(firstDate, toDate, status, pageable);
        } else if (user != null) {
            return getAllPendingByUser(actualUser, pageable);
        } else if (fromDate != null) {
            return getAllPendingByReqDate(fromDate, todayDate, pageable);
        } else if (toDate != null){
            return getAllPendingByReqDate(firstDate, toDate, pageable);
        } else if (status != null) {
            return getAllByRequestStatus(status, pageable);
        }
        return getAllByRequestStatus(RequestStatus.PENDING, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllByRequestStatusAndRequestDate(Date fromDate, Date toDate, RequestStatus status, Pageable pageable) {
        toDate = incrementByOneDay(toDate);
        return policyRequestRepository.findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(fromDate, toDate, status, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllByUserAndRequestStatus(User user, RequestStatus status, Pageable pageable) {
        return policyRequestRepository.findAllByUserAndRequestStatusOrderByRequestDateDesc(user, status, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllPendingByUser(User user, Pageable pageable) {
        return policyRequestRepository.findAllByUserAndRequestStatusOrderByRequestDateDesc(user, RequestStatus.PENDING, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllByUser(User user, Pageable pageable) {
        return policyRequestRepository.findAllByUserAndRequestStatusIsNotOrderByRequestDateDesc(user, RequestStatus.CANCELED, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllPendingByUserAndRequestDate(User user, Date fromDate, Date toDate, Pageable pageable) {
        toDate = incrementByOneDay(toDate);
        return policyRequestRepository.findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, fromDate, toDate, RequestStatus.PENDING, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllByUserAndRequestDateAndStatus(User user, Date fromDate, Date toDate, RequestStatus status, Pageable pageable) {
        toDate = incrementByOneDay(toDate);
        return policyRequestRepository.findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(user, fromDate, toDate, status, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllSortedByReqDate() {
        return new PageImpl<>(policyRequestRepository.findAll(new Sort(Sort.Direction.DESC, "requestDate")));
    }

    @Override
    public void approveRequestById(Long id, User processedBy) {
        PolicyRequest policyRequest = getById(id);
        policyRequest.setRequestStatus(RequestStatus.APPROVED);
        policyRequest.setProcessedBy(processedBy);
        policyRequestRepository.save(policyRequest);
    }

    @Override
    public void declineRequestById(Long id, User processedBy) {
        PolicyRequest policyRequest = getById(id);
        policyRequest.setRequestStatus(RequestStatus.DECLINED);
        policyRequest.setProcessedBy(processedBy);
        policyRequestRepository.save(policyRequest);
    }

    @Override
    public void cancelRequestById(Long id) {
        PolicyRequest policyRequest = getById(id);
        policyRequest.setRequestStatus(RequestStatus.CANCELED);
        policyRequestRepository.save(policyRequest);
    }

    @Override
    public Page<PolicyRequest> getAllPendingByReqDate(Date fromDate, Date toDate, Pageable pageable) {
        toDate = incrementByOneDay(toDate);
        return policyRequestRepository.findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(fromDate, toDate,RequestStatus.PENDING, pageable);
    }

    @Override
    public Page<PolicyRequest> getAllByRequestStatus(RequestStatus requestStatus, Pageable pageable) {
        return policyRequestRepository.findAllByRequestStatusOrderByRequestDateDesc(requestStatus, pageable);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void markOldRequestsAsExpired() {
        LocalDate dateForExpiration = LocalDate.now().minus(Period.ofDays(29));
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateForExpiration.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        policyRequestRepository.findAllByRequestStatusAndRequestDateBefore(RequestStatus.APPROVED, date)
                .forEach(policyRequest -> {
                    policyRequest.setRequestStatus(RequestStatus.EXPIRED);
                    policyRequestRepository.save(policyRequest);
                });
    }

    @Override
    public boolean checkCurrentTime(boolean isClosed, PolicyRequest request){
        if(request.getEffectiveDate().isEqual(LocalDate.now())){
            Calendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour >= 20){
                isClosed = true;
            }
        }
        return isClosed;
    }

    private Date incrementByOneDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
        return date;
    }
}
