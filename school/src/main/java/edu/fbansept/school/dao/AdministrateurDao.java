package edu.fbansept.school.dao;

import edu.fbansept.school.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurDao extends JpaRepository<Administrateur, Integer > {
}