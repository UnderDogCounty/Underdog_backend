package com.underdogCounty.underdogCountyProject.domain.work.service;

import com.underdogCounty.underdogCountyProject.domain.exception.WrongWork;
import com.underdogCounty.underdogCountyProject.domain.global.S3.S3uploader.S3uploader;
import com.underdogCounty.underdogCountyProject.domain.work.Category;
import com.underdogCounty.underdogCountyProject.domain.work.Work;
import com.underdogCounty.underdogCountyProject.domain.work.dto.WorkRequestDto;
import com.underdogCounty.underdogCountyProject.domain.work.dto.WorkResponseDto;
import com.underdogCounty.underdogCountyProject.domain.work.repository.WorkRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class WorkService {

    private final WorkRepository workRepository;
    @Autowired
    private S3uploader s3uploader;

    public Work create(WorkRequestDto workRequestDto) {
        Work work = new Work();
        work.requestToEntity(workRequestDto);
        workRepository.save(work);
        return work;
    }

    public List<WorkResponseDto> getAll() {
        List<Work> works = workRepository.findAll();
        List<WorkResponseDto> result = works.stream()
            .map(s -> new WorkResponseDto().entityToResponse(s))
            .collect(Collectors.toList());
        return result;
    }

    public WorkResponseDto getOne(Long id) {
        Optional<Work> work = workRepository.findById(id);
        if (!work.isPresent()) {
            throw new WrongWork();
        }
        WorkResponseDto result = new WorkResponseDto().entityToResponse(work.get());
        return result;
    }

    public List<WorkResponseDto> getCategory(Category category) {
        List<Work> works = workRepository.findByCategory(category);
        List<WorkResponseDto> result = works.stream()
            .map(s -> new WorkResponseDto().entityToResponse(s))
            .collect(Collectors.toList());
        return result;
    }

    @Transactional
    public WorkRequestDto update(Long id, WorkRequestDto workRequestDto) {
        Optional<Work> work = workRepository.findById(id);
        if (!work.isPresent()) {
            throw new WrongWork();
        }
        work.get().requestToEntity(workRequestDto);
        workRepository.save(work.get());
        return workRequestDto;
    }

    public Long delete(Long id) {
        Optional<Work> work = workRepository.findById(id);
        if (!work.isPresent()) {
            throw new WrongWork();
        }
        workRepository.delete(work.get());
        return id;
    }

    @Transactional
    public Work uploadS3Image(Long id, MultipartFile image) throws IOException {
        Work work = workRepository.findById(id).orElseThrow(
            () -> new WrongWork()
        );
        if (!image.isEmpty()) {
            String storedFileName = s3uploader.upload(image, "images");
            work.updateImageUrl(work, storedFileName);
        }
        return workRepository.save(work);
    }

    @Transactional
    public Work deleteS3Image(Long id) {
        Work work = workRepository.findById(id).orElseThrow(
            () -> new WrongWork()
        );
        work.deleteImageUrl(work);
        return workRepository.save(work);
    }
}
