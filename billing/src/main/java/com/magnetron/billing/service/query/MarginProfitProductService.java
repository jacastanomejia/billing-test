package com.magnetron.billing.service.query;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IMarginProfitProductViewRepo;
import com.magnetron.billing.repository.entity.ProductWithMarginProfit;
import com.magnetron.billing.service.contract.IMarginProfitProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Qualifier("marginProfitProductService")
public class MarginProfitProductService implements IMarginProfitProductService {

    @Autowired
    private IMarginProfitProductViewRepo marginProfitProductViewRepo;

    @Override
    public List<ProductWithMarginProfit> getAll() throws ApiRestRuntimeException {
        return marginProfitProductViewRepo.findAll();
    }
}
