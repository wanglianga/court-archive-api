package com.court.archive.entity;

import lombok.Data;

@Data
public class Borrower {
    private String borrowerId;
    private String borrowerName;
    private String department;
    private String phone;
    private boolean blacklisted;
    private String blacklistReason;
}
