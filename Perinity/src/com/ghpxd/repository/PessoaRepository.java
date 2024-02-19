package com.ghpxd.repository;

import com.ghpxd.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Pessoa findByNomeAndDepartamento(String nome, String departamento);
    List<Pessoa> findByNomeAndPeriodo(String nome, Date inicio, Date fim);
    int countByDepartamento(String departamento);
}