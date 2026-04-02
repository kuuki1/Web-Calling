package com.web_calling.backend.service;

import com.web_calling.backend.entity.Relationship;
import com.web_calling.backend.repository.PersonRepository;
import com.web_calling.backend.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    @Autowired
    private RelationshipRepository relationshipRepo;

    @Autowired
    private PersonRepository personRepo;

    public Map<String, List<String>> buildGraph() {
        Map<String, List<String>> graph = new HashMap<>();

        List<Relationship> relations = relationshipRepo.findAll();

        for (Relationship r : relations) {
            String p1 = personRepo.findById(r.getPerson1Id()).get().getName();
            String p2 = personRepo.findById(r.getPerson2Id()).get().getName();

            graph.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);
            graph.computeIfAbsent(p2, k -> new ArrayList<>()).add(p1);
        }

        return graph;
    }
}