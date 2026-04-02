package com.web_calling.backend;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.web_calling.backend.service.BFSService;
import com.web_calling.backend.service.GraphService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BackendApplication {

	@Autowired
	GraphService graphService;

	@Autowired
	BFSService bfsService;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void testGraph() {
		Map<String, List<String>> graph = graphService.buildGraph();
		System.out.println(graph);
	}

	@PostConstruct
	public void testBFS() {
		Map<String, List<String>> graph = graphService.buildGraph();

		System.out.println("Graph: " + graph);

		List<String> path = bfsService.bfs("A", "D", graph);

		System.out.println("Path: " + path);
	}
}
