package com.web_calling.backend.controller;

import com.web_calling.backend.service.BFSService;
import com.web_calling.backend.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ConnectionController {

    @Autowired
    private GraphService graphService;

    @Autowired
    private BFSService bfsService;

    @GetMapping("/connection")
    public Map<String, Object> findConnection(
            @RequestParam String from,
            @RequestParam String to
    ) {

        Map<String, Object> response = new HashMap<>();

        // normalize input
        from = normalize(from);
        to = normalize(to);

        // build graph
        Map<Long, List<Long>> graph = graphService.buildGraph();

        Long startId = graphService.getIdByName(from);
        Long targetId = graphService.getIdByName(to);

        if (startId == null || targetId == null) {
            response.put("message", "Person not found");
            response.put("path", new ArrayList<>());
            response.put("distance", -1);
            return response;
        }

        List<Long> pathIds = bfsService.bfs(startId, targetId, graph);

        if (pathIds.isEmpty()) {
            response.put("message", "No connection found");
            response.put("path", new ArrayList<>());
            response.put("distance", -1);
        } else {
            List<String> pathNames = new ArrayList<>();

            for (Long id : pathIds) {
                pathNames.add(graphService.getNameById(id));
            }

            response.put("path", pathNames);
            response.put("distance", pathNames.size() - 1);
        }

        return response;
    }

    private String normalize(String name) {
        return name.trim().toLowerCase().replace(" ", "_");
    }
}