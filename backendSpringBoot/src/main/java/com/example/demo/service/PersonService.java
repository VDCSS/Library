package com.example.demo.service;

import com.example.demo.dto.PersonDTO;
import com.example.demo.mapper.PersonMapper;
import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository repo;
    private final PersonMapper mapper;

    public PersonService(PersonRepository repo, PersonMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<PersonDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) {
        Person p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
        return mapper.toDTO(p);
    }

    @Transactional
    public PersonDTO create(PersonDTO dto) {
        Person p = mapper.toEntity(dto);
        // NOTE: password hashing should be added (BCrypt) in production
        Person saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public PersonDTO update(Long id, PersonDTO dto) {
        Person existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
        existing.setName(dto.getName());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        existing.setUsername(dto.getUsername());
        existing.setPhone(dto.getPhone());
        Person saved = repo.save(existing);
        return mapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Person not found: " + id);
        repo.deleteById(id);
    }
}
