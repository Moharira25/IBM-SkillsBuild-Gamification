package com.example.ibm_project_code.database;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BadgeCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Badge badge;

    @Column
    private boolean complete;

    @Column
    private int counter;

    public void updateCounter(){
        this.counter+=1;
        if (this.counter >= this.badge.getRequirement()){
            this.complete = true;
        }
    }
}
