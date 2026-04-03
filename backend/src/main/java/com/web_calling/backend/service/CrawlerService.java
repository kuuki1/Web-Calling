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
    private RelationshipRepository relationshipRepo;

    @Autowired
    private GraphService graphService;

    private Map<String, Person> personCache = new HashMap<>();
    private Set<String> relationshipCache = new HashSet<>();

    public void crawlMultiLevel(String startName, int maxDepth) {

        startName = StringUtils.normalize(startName);

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(startName);
        visited.add(startName);

        int depth = 0;

        while (!queue.isEmpty() && depth < maxDepth) {

            int size = queue.size();

            System.out.println("=== LEVEL " + depth + " ===");

            for (int i = 0; i < size; i++) {

                String current = queue.poll();

                List<String> links = wikiService.getLinks(current);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                Person main = findOrCreate(current);

                int limit = Math.min(links.size(), 10);

                for (int j = 0; j < limit; j++) {

                    String linkName = StringUtils.normalize(links.get(j));

                    Person related = findOrCreate(linkName);

                    if (!existsRelationship(main.getId(), related.getId())) {

                        Relationship r = new Relationship();
                        r.setPerson1Id(main.getId());
                        r.setPerson2Id(related.getId());

                        relationshipRepo.save(r);

                        // cache relationship
                        relationshipCache.add(main.getId() + "-" + related.getId());
                    }

                    if (!visited.contains(linkName)) {
                        queue.add(linkName);
                        visited.add(linkName);
                    }
                }
            }

            depth++;
        }

        graphService.clearCache();

        System.out.println("Multi-level crawl DONE");
    }

    private Person findOrCreate(String name) {

        if (personCache.containsKey(name)) {
            return personCache.get(name);
        }

        Person p = personRepo.findByName(name)
                .orElseGet(() -> {
                    Person newP = new Person();
                    newP.setName(name);
                    return personRepo.save(newP);
                });

        personCache.put(name, p);

        return p;
    }

    private boolean existsRelationship(Long p1, Long p2) {

        String key1 = p1 + "-" + p2;
        String key2 = p2 + "-" + p1;

        if (relationshipCache.contains(key1) || relationshipCache.contains(key2)) {
            return true;
        }

        boolean exists = relationshipRepo.existsByPerson1IdAndPerson2Id(p1, p2)
                || relationshipRepo.existsByPerson1IdAndPerson2Id(p2, p1);

        if (exists) {
            relationshipCache.add(key1);
        }

        return exists;
    }
}