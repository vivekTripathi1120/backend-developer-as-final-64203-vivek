package com.project.resource_booking_system.repsotiory;

import com.project.resource_booking_system.entity.Role;
import com.project.resource_booking_system.utils.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName roleName);
}