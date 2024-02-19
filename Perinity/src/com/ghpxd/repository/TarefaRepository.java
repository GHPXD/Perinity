package com.ghpxd.repository;

import com.ghpxd.model.Tarefa;
import com.ghpxd.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findTop3ByPessoaIsNullOrderByPrazoAsc();
    List<Tarefa> findByPessoa(Pessoa pessoa);
    List<Tarefa> findByPessoaAndPrazoBetween(Pessoa pessoa, Date inicio, Date fim);
    int countByDepartamento(String departamento);
}