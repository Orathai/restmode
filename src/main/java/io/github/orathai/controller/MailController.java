package io.github.orathai.controller;

import org.springframework.stereotype.Service;

@Service
public class MailController {

    public boolean send(String customerEmail) {
        return customerEmail != null;
    }
}
