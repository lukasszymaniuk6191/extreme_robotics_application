package com.lukas.er.monitoring.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class RateDataDto {

    private Date tableDate;
    private String currency;
    private String code;
    private String mid;

}
