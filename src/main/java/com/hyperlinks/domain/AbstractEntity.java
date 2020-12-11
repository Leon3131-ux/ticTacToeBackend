package com.hyperlinks.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@MappedSuperclass
@EqualsAndHashCode
@Data
public class AbstractEntity {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    AbstractEntity() {
        this.id = null;
    }

    AbstractEntity(Long id) {
        this.id = id;
    }

}
