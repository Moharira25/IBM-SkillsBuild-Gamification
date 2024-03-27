package com.example.ibm_project_code.database;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column
    private int requirement;

    // figured I should keep the image and type separate, since badges
    // can share the same image but not the same type (colour)
    @Column(nullable = false)
    private String image;

    @Column
    private int type;

    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
    private List<BadgeCollection> badgeCollections = new ArrayList<>();


}
