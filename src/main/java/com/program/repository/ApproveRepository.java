package com.program.repository;

import com.program.model.approve.Approve;
import com.program.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApproveRepository extends JpaRepository<Approve, Integer> {

    @Query("SELECT a FROM Approve a WHERE a.approveName =:approveName")
    Approve findApproveByName(String approveName);

}
