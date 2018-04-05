package com.lukas.er.monitoring.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ExceptionDto
{
    private String errCode;
    private String errClassName;
    private String errMsg;
}

