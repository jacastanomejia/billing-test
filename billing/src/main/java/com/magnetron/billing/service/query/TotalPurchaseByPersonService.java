package com.magnetron.billing.service.query;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.ITotalPurchaseByPersonViewRepo;
import com.magnetron.billing.repository.entity.PersonWithTotalPurchase;
import com.magnetron.billing.service.contract.ITotalPurchaseByPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Qualifier("totalPurchaseService")
public class TotalPurchaseByPersonService implements ITotalPurchaseByPersonService {

    @Autowired
    private ITotalPurchaseByPersonViewRepo saleTotalViewRepo;

    @Override
    public List<PersonWithTotalPurchase> getAll() throws ApiRestRuntimeException {
        return saleTotalViewRepo.findAll();
    }
}
