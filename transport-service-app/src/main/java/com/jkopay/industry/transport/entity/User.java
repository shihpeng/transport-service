package com.jkopay.industry.transport.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    Long id;
    Date gmtCreated;
    String name;
}
