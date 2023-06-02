package com.program.repository;

import com.program.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    @Query("SELECT d FROM Department d WHERE d.departmentId = :departmentId")
    Department findByDepartmentId(Integer departmentId);

    @Query("SELECT d FROM Department d WHERE d.departmentName = :departmentName")
    Department findByDepartmentName(String departmentName);

}
