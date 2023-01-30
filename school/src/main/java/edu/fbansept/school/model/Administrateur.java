package edu.fbansept.school.model;

import javax.persistence.Entity;

@Entity
public class Administrateur extends User {

    private boolean superAdmin;
}
