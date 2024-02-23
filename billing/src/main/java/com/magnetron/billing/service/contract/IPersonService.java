package com.magnetron.billing.service.contract;

import com.magnetron.billing.service.contract.base.ICrudService;
import com.magnetron.billing.service.contract.base.IQueryByIdService;
import com.magnetron.billing.service.dto.PersonDto;

public interface IPersonService
        extends ICrudService<PersonDto, Long>,
                IQueryByIdService<PersonDto, Long> {
}
