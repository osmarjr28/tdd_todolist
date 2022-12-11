package com.generation.todolist.controller;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TarefaController {

        @Autowired
        private TarefaRepository tarefaRepository;

        @PostMapping
        public ResponseEntity<Tarefa> post(@Valid @RequestBody Tarefa tarefa){
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefaRepository.save(tarefa));
        }

    }

