package com.lukas.er.monitoring.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class FileWatcherDataDto {

    private String filename;
    private String eventName;

}
