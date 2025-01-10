package com.example.server.dao;

import com.example.server.model.BkavUser;
import com.example.server.model.view.BkavUserDeviceView;
import com.example.server.repository.view.BkavUserDeviceViewRepository;
import com.example.server.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaBkavUserDao implements EntityDao<BkavUser>{

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BkavUserDeviceViewRepository bkavUserDeviceViewRepository;

    @Override
    public Optional<BkavUser> get(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<BkavUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public BkavUser save(BkavUser bkavUser) {
        return userRepository.save(bkavUser);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public List<BkavUserDeviceView> getUserByGenderAndName(String gender,String name){
        StringBuilder queryStr = new StringBuilder("SELECT v FROM BkavUserDeviceView v WHERE 1=1");

        if (gender != null) {
            queryStr.append(" AND v.gender = :gender");
        }

        if (name != null) {
            queryStr.append(" AND LOWER(v.name) LIKE LOWER(CONCAT('%', :name, '%'))");
        }
        TypedQuery<BkavUserDeviceView> query = entityManager.createQuery(queryStr.toString(), BkavUserDeviceView.class);

        if (gender != null) {
            query.setParameter("gender", gender);
        }

        if (name != null) {
            query.setParameter("name", name);
        }

        System.out.println(queryStr);

        return query.getResultList();
    }
}
