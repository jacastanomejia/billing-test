package com.magnetron.billing.repository;

import com.magnetron.billing.repository.model.BillHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBillHeaderRepo extends JpaRepository<BillHeader, Long> {

    BillHeader findByNumber(Integer number);
}
