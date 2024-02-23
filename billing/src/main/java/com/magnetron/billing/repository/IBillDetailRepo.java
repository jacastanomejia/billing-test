package com.magnetron.billing.repository;

import com.magnetron.billing.repository.entity.BillDetail;
import com.magnetron.billing.repository.entity.BillHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBillDetailRepo extends JpaRepository<BillDetail, Long> {

    public List<BillDetail> findByBillHeader(BillHeader bill);
}
