package com.example.demo.services;

import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


public interface PolicyRequestService {
    Page<PolicyRequest> getAll(Pageable pageable);

    PolicyRequest getById(Long id);

    PolicyRequest create(PolicyRequest policyRequest, MultipartFile img);

    Page<PolicyRequest> get(String user, Date fromDate, Date toDate, RequestStatus status, Pageable pageable);

    Page<PolicyRequest> getAllByUserAndRequestStatus(User user, RequestStatus status, Pageable pageable);

    Page<PolicyRequest> getAllByRequestStatusAndRequestDate(Date fromDate, Date toDate, RequestStatus status, Pageable pageable);

    Page<PolicyRequest> getAllPendingByUser(User user, Pageable pageable);

    Page<PolicyRequest> getAllByUser(User user, Pageable pageable);

    Page<PolicyRequest> getAllPendingByUserAndRequestDate(User user, Date fromDate, Date toDate, Pageable pageable);

    Page<PolicyRequest> getAllByUserAndRequestDateAndStatus(User user, Date fromDate, Date toDate, RequestStatus status, Pageable pageable);

    Page<PolicyRequest> getAllSortedByReqDate();

    void approveRequestById(Long id, User processedBy);

    void declineRequestById(Long id, User processedBy);

    void cancelRequestById(Long id);

    Page<PolicyRequest> getAllPendingByReqDate(Date fromDate, Date toDate, Pageable pageable);

    Page<PolicyRequest> getAllByRequestStatus(RequestStatus requestStatus, Pageable pageable);

    void markOldRequestsAsExpired();

    boolean checkCurrentTime(boolean isClosed, PolicyRequest request);
}
