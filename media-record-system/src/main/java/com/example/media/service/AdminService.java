package com.example.media.service;

import com.example.media.entity.Admin;

public interface AdminService {

    Admin login(String username, String password);

}