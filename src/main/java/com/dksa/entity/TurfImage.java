package com.dksa.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "turf_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurfImage {

    @Id
    @GeneratedValue(
        strategy =
        GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(
        name = "turf_id")
    private Turf turf;
}