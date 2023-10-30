package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name ="transfers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne
    private Customer sender;
    @ManyToOne
    private Customer recipient;
    private BigDecimal transferAmount;
    private Long fees;
    private BigDecimal feesAmount;
    private BigDecimal transactionAmount;
    private LocalDateTime localDateTime;


}