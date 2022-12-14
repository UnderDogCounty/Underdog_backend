package com.underdogCounty.underdogCountyProject.domain.artist.repository;

import com.underdogCounty.underdogCountyProject.domain.artist.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
