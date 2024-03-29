package com.underdogCounty.underdogCountyProject.domain.artist.service;

import com.underdogCounty.underdogCountyProject.domain.artist.dto.ArtistRequestDto;
import com.underdogCounty.underdogCountyProject.domain.artist.dto.ArtistResponseDto;
import com.underdogCounty.underdogCountyProject.domain.artist.entity.Artist;
import com.underdogCounty.underdogCountyProject.domain.artist.repository.ArtistRepository;
import com.underdogCounty.underdogCountyProject.domain.exception.WrongArtist;
import com.underdogCounty.underdogCountyProject.domain.global.S3.S3uploader.S3uploader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    private S3uploader s3uploader;

    public Artist create(ArtistRequestDto artistRequestDto) {
        Artist artist = new Artist();
        artist.requestToEntity(artistRequestDto);
        artistRepository.save(artist);
        return artist;
    }

    public List<ArtistResponseDto> getAll() {
        List<Artist> artists = artistRepository.findAll();
        List<ArtistResponseDto> result = artists.stream()
            .map(s -> new ArtistResponseDto().entityToResponse(s)).collect(
                Collectors.toList());
        return result;
    }

    public ArtistResponseDto getOne(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (!artist.isPresent()) {
            throw new WrongArtist();
        }
        ArtistResponseDto result = new ArtistResponseDto().entityToResponse(artist.get());
        return result;
    }

    public ArtistRequestDto update(Long id, ArtistRequestDto artistRequestDto) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (!artist.isPresent()) {
            throw new WrongArtist();
        }
        artist.get().requestToEntity(artistRequestDto);
        artistRepository.save(artist.get());
        return artistRequestDto;
    }

    public Long delete(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (!artist.isPresent()) {
            throw new WrongArtist();
        }
        artistRepository.deleteById(id);
        return id;
    }

    @Transactional
    public Artist uploadS3Image(Long id, MultipartFile image) throws IOException {
        Artist artist = artistRepository.findById(id).orElseThrow(
            () -> new WrongArtist()
        );
        if (!image.isEmpty()) {
            String storedFileName = s3uploader.upload(image, "images");
            artist.updateImageUrl(artist, storedFileName);
        }
        return artistRepository.save(artist);
    }

    @Transactional
    public Artist deleteS3Image(Long id) {
        Artist artist = artistRepository.findById(id).orElseThrow(
            () -> new WrongArtist()
        );
        artist.deleteImageUrl(artist);
        return artistRepository.save(artist);
    }
}
