package com.lukas.er.monitoring.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "buy_and_sell_rates")
public class BuyAndSellRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "table_name")
    private String table;
    private String no;
    private Date tradingDate;
    private Date effectiveDate;
    private Date tableDate;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buy_and_sell_rate_id", referencedColumnName = "id")
    List<TradingRates> rates = new ArrayList<>();
    private String fileName;

}
