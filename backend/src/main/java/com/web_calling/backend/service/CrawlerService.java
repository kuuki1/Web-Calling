package com.web_calling.backend.service;

import com.web_calling.backend.entity.Person;
import com.web_calling.backend.entity.Relationship;
import com.web_calling.backend.repository.PersonRepository;
import com.web_calling.backend.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void crawlAndSave(String name) {

        name = normalize(name);

        List<String> links = wikiService.getLinks(name);

        Person main = findOrCreate(name);

        int limit = Math.min(links.size(), 20);

        for (int i = 0; i < limit; i++) {

            String linkName = normalize(links.get(i));

            Person related = findOrCreate(linkName);

            if (!existsRelationship(main.getId(), related.getId())) {

                Relationship r = new Relationship();
                r.setPerson1Id(main.getId());
                r.setPerson2Id(related.getId());

                relationshipRepo.save(r);
            }
        }

        graphService.clearCache();

        System.out.println("Crawl done: " + name);
    }

    private Person findOrCreate(String name) {
        return personRepo.findByName(name)
                .orElseGet(() -> {
                    Person p = new Person();
                    p.setName(name);
                    return personRepo.save(p);
                });
    }

    private boolean existsRelationship(Long p1, Long p2) {
        return relationshipRepo.existsByPerson1IdAndPerson2Id(p1, p2)
                || relationshipRepo.existsByPerson1IdAndPerson2Id(p2, p1);
    }

    private String normalize(String name) {
        return name.trim();
    }
}