package com.magnetron.billing.service.query;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IGrossProfitProductViewRepo;
import com.magnetron.billing.repository.entity.ProductWithGrossProfit;
import com.magnetron.billing.service.contract.IGrossProfitProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Qualifier("grossProfitProductService")
public class GrossProfitProductService implements IGrossProfitProductService {

    @Autowired
    private IGrossProfitProductViewRepo grossProfitProductViewRepo;

    @Override
    public List<ProductWithGrossProfit> getAll() throws ApiRestRuntimeException {
        return grossProfitProductViewRepo.findAll();
    }
}
