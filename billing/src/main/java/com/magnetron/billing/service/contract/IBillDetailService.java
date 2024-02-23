package com.magnetron.billing.service.contract;

import com.magnetron.billing.service.contract.base.ICrudService;
import com.magnetron.billing.service.contract.base.IQueryByIdService;
import com.magnetron.billing.service.contract.base.IQueryByReferenceService;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillDetailFullDto;

public interface IBillDetailService
        extends ICrudService<BillDetailDto, Long>,
                IQueryByIdService<BillDetailFullDto, Long>,
                IQueryByReferenceService<BillDetailDto, Long>
{
}
