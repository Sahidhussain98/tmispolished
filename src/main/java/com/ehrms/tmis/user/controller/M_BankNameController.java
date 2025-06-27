package com.tmisehrms.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_BankName;
import com.tmisehrms.user.service.M_BankNameService;

import java.util.List;

@RestController
@RequestMapping("/api/banks")
public class M_BankNameController {

    private final M_BankNameService m_BankNameService;

    public M_BankNameController(M_BankNameService m_BankNameService) {
        this.m_BankNameService = m_BankNameService;
    }

    @GetMapping
    public List<M_BankName> getAllBanks() {
        return m_BankNameService.getAllBanks();
    }
}
