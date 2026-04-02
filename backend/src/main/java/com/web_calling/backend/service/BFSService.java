package com.web_calling.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BFSService {

    public List<Long> bfs(Long start, Long target, Map<Long, List<Long>> graph) {

        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return new ArrayList<>();
        }

        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        Map<Long, Long> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Long current = queue.poll();

            if (current.equals(target)) {
                break;
            }

            List<Long> neighbors = graph.get(current);
            if (neighbors == null) continue;

            for (Long neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (!parent.containsKey(target) && !start.equals(target)) {
            return new ArrayList<>();
        }

        List<Long> path = new ArrayList<>();
        Long step = target;

        while (step != null) {
            path.add(step);
            step = parent.get(step);
        }

        Collections.reverse(path);

        return path;
    }
}