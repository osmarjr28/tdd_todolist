package com.generation.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.todolist.model.Tarefa;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long>{

    public List<Tarefa> findAllByNomeContainingIgnoreCase( String nome);

}
