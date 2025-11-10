package com.health.service;

import com.health.entity.Entity;
import com.health.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailService {
    @Autowired
    private DetailRepository detailRepository;
    public Entity getEntity(int id ) {
        return detailRepository.findById(id);
    }
}
