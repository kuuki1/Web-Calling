package com.web_calling.backend.service;

import com.web_calling.backend.entity.Person;
import com.web_calling.backend.entity.Relationship;
import com.web_calling.backend.repository.PersonRepository;
import com.web_calling.backend.repository.RelationshipRepository;
import com.web_calling.backend.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    @Autowired
    private RelationshipRepository relationshipRepo;

    @Autowired
    private PersonRepository personRepo;

    private Map<Long, List<Long>> cachedGraph = new HashMap<>();
    private Map<Long, String> idToName = new HashMap<>();
    private Map<String, Long> nameToId = new HashMap<>();

    public Map<Long, List<Long>> buildGraph() {

        cachedGraph.clear();
        idToName.clear();
        nameToId.clear();

        List<Relationship> relations = relationshipRepo.findAll();
        List<Person> persons = personRepo.findAll();

        for (Person p : persons) {
            String normalized = StringUtils.normalize(p.getName());

            idToName.put(p.getId(), p.getName()); // giữ nguyên để trả ra đẹp
            nameToId.put(normalized, p.getId());
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
        return nameToId.get(StringUtils.normalize(name));
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