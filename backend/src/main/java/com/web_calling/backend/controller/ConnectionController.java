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

        // Build graph từ DB
        Map<String, List<String>> graph = graphService.buildGraph();

        // Chạy BFS
        List<String> path = bfsService.bfs(from, to, graph);

        if (path.isEmpty()) {
            response.put("message", "No connection found");
            response.put("path", new ArrayList<>());
            response.put("distance", -1);
        } else {
            response.put("path", path);
            response.put("distance", path.size() - 1);
        }

        return response;
    }
}