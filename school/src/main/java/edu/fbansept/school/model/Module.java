package edu.fbansept.school.model;

import com.fasterxml.jackson.annotation.JsonView;
import edu.fbansept.school.view.ModuleView;
import edu.fbansept.school.view.UserView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserView.class)
    private  Integer id;

    @JsonView({UserView.class, ModuleView.class})
    private String name;


    @JsonView(ModuleView.class)
    private  String description;



    @ManyToMany(mappedBy = "listModule")
    @JsonView(ModuleView.class)
    Set<User> listUser = new HashSet<>();

   @ManyToOne
   Administrateur referent;
}
