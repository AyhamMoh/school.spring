package edu.fbansept.school.dao;

import edu.fbansept.school.model.Role;
import edu.fbansept.school.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer > {

}


