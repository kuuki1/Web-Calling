package com.web_calling.backend;

import java.util.*;

public class BFSExample {
    public static void main(String[] args) {

        Map<String, List<String>> graph = new HashMap<>();

        // tạo graph
        graph.put("A", Arrays.asList("B"));
        graph.put("B", Arrays.asList("A", "C"));
        graph.put("C", Arrays.asList("B", "D"));
        graph.put("D", Arrays.asList("C"));

        List<String> path = bfs("A", "D", graph);

        System.out.println(String.join(" -> ", path));
    }

    public static List<String> bfs(String start, String target, Map<String, List<String>> graph) {

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(target)) break;

            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // build path
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
