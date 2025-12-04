package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final UserRepository repo;
    private final PersonMapper mapper;

    public PersonService(UserRepository repo, PersonMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
        return mapper.toDTO(p);
    }

    @Transactional
    public UserDTO create(UserDTO dto) {
        User p = mapper.toEntity(dto);
        User saved = repo.save(p);
        return mapper.toDTO(saved);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found: " + id));
        existing.setName(dto.getName());
        existing.setCpf(dto.getCpf());
        existing.setEmail(dto.getEmail());
        existing.setUsername(dto.getUsername());
        existing.setPhone(dto.getPhone());
        User saved = repo.save(existing);
        return mapper.toDTO(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Person not found: " + id);
        repo.deleteById(id);
    }
}
