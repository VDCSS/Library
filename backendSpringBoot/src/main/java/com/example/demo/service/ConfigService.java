package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ConfigService {
    @Value("${library.taxPerDay}")
    private BigDecimal taxPerDay;
    @Value("${library.graceHours}")
    private Integer graceHours;
    @Value("${library.damageFixed}")
    private BigDecimal damageFixed;
    public BigDecimal getRatePerDay(){return taxPerDay;}
    public int getGraceHours(){return graceHours;}
    public BigDecimal getDamageFixed(){return damageFixed;}
}
