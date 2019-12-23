package com.example.demo.repositories;

import com.example.demo.models.Enums.RequestStatus;
import com.example.demo.models.PolicyRequest;
import com.example.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRequestRepository extends JpaRepository<PolicyRequest, Long> {
    Page<PolicyRequest> findAllByUserAndRequestStatusOrderByRequestDateDesc(User user, RequestStatus requestStatus, Pageable pageable);
    Page<PolicyRequest> findAllByUserAndRequestStatusIsNotOrderByRequestDateDesc(User user, RequestStatus requestStatus, Pageable pageable);
    List<PolicyRequest> findAllByRequestStatusAndRequestDateBefore(RequestStatus status, Date date);
    Page<PolicyRequest> findAllByRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(Date beforeDate, Date afterDate, RequestStatus requestStatus, Pageable pageable);
    Page<PolicyRequest> findAllByRequestStatusOrderByRequestDateDesc(RequestStatus requestStatus, Pageable pageable);
    Page<PolicyRequest> findAllByUserAndRequestDateGreaterThanEqualAndRequestDateLessThanAndRequestStatusOrderByRequestDateDesc(User user , Date beforeDate, Date afterDate, RequestStatus requestStatus, Pageable pageable);
}

