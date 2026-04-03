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
public class CrawlerService {

    @Autowired
    private WikiService wikiService;

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private RelationshipRepository relationRepo;

    public void crawlAndSave(String startName, int maxDepth) {

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(startName);
        visited.add(startName);

        int level = 0;

        while (!queue.isEmpty() && level <= maxDepth) {

            int size = queue.size();
            System.out.println("=== LEVEL " + level + " ===");

            for (int i = 0; i < size; i++) {

                String current = queue.poll();

                String normalizedCurrent = StringUtils.normalize(current);

                Person currentPerson = personRepo
                        .findByName(normalizedCurrent)
                        .orElseGet(() -> personRepo.save(createPerson(normalizedCurrent)));

                List<String> links = wikiService.getLinks(current);

                for (String link : links) {

                    String normalizedLink = StringUtils.normalize(link);

                    Person neighbor = personRepo
                            .findByName(normalizedLink)
                            .orElseGet(() -> personRepo.save(createPerson(normalizedLink)));

                    if (!relationshipExists(currentPerson.getId(), neighbor.getId())) {
                        relationRepo.save(createRelation(currentPerson.getId(), neighbor.getId()));
                    }

                    if (!visited.contains(link)) {
                        visited.add(link);
                        queue.add(link);
                    }
                }
            }

            level++;
        }

        System.out.println("Crawl DONE");
    }

    // ===== helper =====

    private Person createPerson(String name) {
        Person p = new Person();
        try {
            java.lang.reflect.Field field = Person.class.getDeclaredField("name");
            field.setAccessible(true);
            field.set(p, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    private Relationship createRelation(Long p1, Long p2) {
        Relationship r = new Relationship();
        try {
            java.lang.reflect.Field f1 = Relationship.class.getDeclaredField("person1Id");
            java.lang.reflect.Field f2 = Relationship.class.getDeclaredField("person2Id");

            f1.setAccessible(true);
            f2.setAccessible(true);

            f1.set(r, p1);
            f2.set(r, p2);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    private boolean relationshipExists(Long p1, Long p2) {
        return relationRepo.findAll().stream().anyMatch(r ->
                (r.getPerson1Id().equals(p1) && r.getPerson2Id().equals(p2)) ||
                (r.getPerson1Id().equals(p2) && r.getPerson2Id().equals(p1))
        );
    }
}