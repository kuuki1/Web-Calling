package com.web_calling.backend.service;

import com.web_calling.backend.entity.Person;
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

    // cache graph
    private Map<Long, List<Long>> cachedGraph = new HashMap<>();

    // cache id -> name
    private Map<Long, String> idToName = new HashMap<>();

    // cache name -> id
    private Map<String, Long> nameToId = new HashMap<>();

    public Map<Long, List<Long>> buildGraph() {

        if (!cachedGraph.isEmpty()) {
            return cachedGraph;
        }

        List<Relationship> relations = relationshipRepo.findAll();
        List<Person> persons = personRepo.findAll();

        // build map id <-> name
        for (Person p : persons) {
            idToName.put(p.getId(), p.getName());
            nameToId.put(p.getName(), p.getId());
        }

        for (Relationship r : relations) {
            Long p1 = r.getPerson1Id();
            Long p2 = r.getPerson2Id();

            cachedGraph.computeIfAbsent(p1, k -> new ArrayList<>()).add(p2);
            cachedGraph.computeIfAbsent(p2, k -> new ArrayList<>()).add(p1);
        }

        return cachedGraph;
    }

    public Long getIdByName(String name) {
        return nameToId.get(name);
    }

    public String getNameById(Long id) {
        return idToName.get(id);
    }

    public void clearCache() {
        cachedGraph.clear();
        idToName.clear();
        nameToId.clear();
    }
}