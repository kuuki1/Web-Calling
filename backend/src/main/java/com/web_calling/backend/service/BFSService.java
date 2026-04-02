package com.web_calling.backend.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BFSService {

    public List<String> bfs(String start, String target, Map<String, List<String>> graph) {

        // Nếu start hoặc target không tồn tại trong graph
        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return new ArrayList<>();
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // Nếu tìm thấy target thì dừng
            if (current.equals(target)) {
                break;
            }

            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Nếu không tìm thấy đường đi
        if (!parent.containsKey(target) && !start.equals(target)) {
            return new ArrayList<>();
        }

        // Build path từ target → start
        List<String> path = new ArrayList<>();
        String step = target;

        while (step != null) {
            path.add(step);
            step = parent.get(step);
        }

        Collections.reverse(path);

        return path;
    }
}