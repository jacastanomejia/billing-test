package com.magnetron.billing.util;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.magnetron.billing.enumeration.DocumentType;
import com.magnetron.billing.enumeration.DomainName;
import com.magnetron.billing.service.dto.BillDetailDto;
import com.magnetron.billing.service.dto.BillHeaderDto;
import com.magnetron.billing.service.dto.PersonDto;
import com.magnetron.billing.service.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntityFactory {

    public static List<Object> getList(DomainName type, int size){
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(create(type));
        }
        return list;
    }

    public static Object create(DomainName type){
        return switch (type) {
            case Person -> createPerson();
            case Product -> createdProduct();
            case BillHeader -> createdBillHeader();
            case BillDetail -> createdBillDetail();
        };
    }

    private static BillHeaderDto createdBillHeader(){
        return BillHeaderDto.builder()
                .number(1234567 + RandomUtil.getPositiveInt())
                .date(LocalDateTime.now())
                .build();
    }

    private static PersonDto createPerson(){
        return PersonDto.builder()
                .name("Juan" + RandomUtil.getPositiveInt())
                .surname("Perez")
                .documentType(DocumentType.CC)
                .documentNumber("2123456" + RandomUtil.getPositiveInt())
                .build();
    }

    private static ProductDto createdProduct(){
        return ProductDto.builder()
                .description("Item" + + RandomUtil.getPositiveInt())
                .price(21_000d)
                .cost(18_000d)
                .unit("Unidad")
                .build();
    }

    private static BillDetailDto createdBillDetail(){
        return BillDetailDto.builder()
                .line("Item" + + RandomUtil.getPositiveInt())
                .quantity(1 + RandomUtil.getPositiveInt())
                .build();
    }
}
