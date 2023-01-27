package com.employeedir.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.employeedir.demo.entity.ProspectLinks;



@Repository
public interface ProspectLinksRepo extends JpaRepository<ProspectLinks, Integer> {

}
