package com.generation.todolist.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TarefaControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Test
    @DisplayName("Criar nova Tarefa")
    public void deveCriarNovaTarefa() throws Exception {

        Tarefa tarefa = new Tarefa(0L, "Tarefa 01", "Tarefa numero 1", "João", LocalDate.now(), true);

        HttpEntity<Tarefa> corpoRequisicao = new HttpEntity<Tarefa>(tarefa);

        ResponseEntity<Tarefa> resposta = testRestTemplate
                .exchange("/tarefas", HttpMethod.POST, corpoRequisicao, Tarefa.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), resposta.getBody().getNome());

    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getAll (){

        return ResponseEntity.ok(tarefaRepository.findAll());
    }

    @Test
    @DisplayName("Listar uma Tarefa Específica")
    public void deveListarApenasUmaTarefa() {

        Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2", "Maria", LocalDate.now(), true));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/tarefas/" + buscaTarefa.getId(), HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getById(@PathVariable Long id) {
        return tarefaRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Tarefa>> getByNome(@PathVariable String nome){
        return ResponseEntity.ok(tarefaRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    @PutMapping
    public ResponseEntity<Tarefa> putPostagem (@Valid @RequestBody Tarefa tarefa){
        return tarefaRepository.findById((tarefa.getId()))
                .map(resposta -> ResponseEntity.status(HttpStatus.OK)
                        .body(tarefaRepository.save(tarefa)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        Optional<Tarefa> postagem = tarefaRepository.findById(id);

        if(postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        tarefaRepository.deleteById(id);
    }

}