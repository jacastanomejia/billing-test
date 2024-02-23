package com.magnetron.billing.service.contract;

import com.magnetron.billing.service.contract.base.ICrudService;
import com.magnetron.billing.service.contract.base.IQueryByIdService;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.BillHeaderFullDto;

public interface IBillHeaderService
        extends ICrudService<BillHeaderDto, Long>,
                IQueryByIdService<BillHeaderFullDto, Long> {
}
