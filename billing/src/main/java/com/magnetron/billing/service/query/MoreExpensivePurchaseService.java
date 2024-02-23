package com.magnetron.billing.service.query;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IMoreExpensivePurchaseViewRepo;
import com.magnetron.billing.repository.entity.PersonMoreExpensivePurchase;
import com.magnetron.billing.service.contract.IMoreExpensivePurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Qualifier("mostExpensiveProductService")
public class MoreExpensivePurchaseService implements IMoreExpensivePurchaseService {

    @Autowired
    private IMoreExpensivePurchaseViewRepo mostExpensiveProductViewRepo;

    @Override
    public List<PersonMoreExpensivePurchase> getAll() throws ApiRestRuntimeException {
        return mostExpensiveProductViewRepo.findAll();
    }
}
