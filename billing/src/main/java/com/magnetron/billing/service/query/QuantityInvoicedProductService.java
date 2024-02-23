package com.magnetron.billing.service.query;

import com.magnetron.billing.exception.ApiRestRuntimeException;
import com.magnetron.billing.repository.IQuantityInvoicedProductViewRepo;
import com.magnetron.billing.repository.entity.ProductWithQuantityInvoiced;
import com.magnetron.billing.service.contract.IQuantityInvoicedProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Qualifier("quantityInvoiceProductService")
public class QuantityInvoicedProductService implements IQuantityInvoicedProductService {

    @Autowired
    private IQuantityInvoicedProductViewRepo quantityInvoicedProductViewRepo;

    @Override
    public List<ProductWithQuantityInvoiced> getAll() throws ApiRestRuntimeException {
        return quantityInvoicedProductViewRepo.findAll();
    }
}
