package com.ghpxd;

import com.ghpxd.model.Pessoa;
import com.ghpxd.model.Tarefa;
import com.ghpxd.repository.PessoaRepository;
import com.ghpxd.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PessoaControllerTest {

    @Autowired
    private PessoaController pessoaController;

    @MockBean
    private PessoaRepository pessoaRepository;

    @Test
    public void testGetPessoas() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setNome("Pessoa 1");
        Pessoa pessoa2 = new Pessoa();
        pessoa2.setNome("Pessoa 2");

        when(pessoaRepository.findAll()).thenReturn(Arrays.asList(pessoa1, pessoa2));

        List<Pessoa> pessoas = pessoaController.getPessoas();

        assertEquals(2, pessoas.size());
        assertEquals("Pessoa 1", pessoas.get(0).getNome());
        assertEquals("Pessoa 2", pessoas.get(1).getNome());
    }
}

@SpringBootTest
public class TarefaControllerTest {

    @Autowired
    private TarefaController tarefaController;

    @MockBean
    private TarefaRepository tarefaRepository;

    @Test
    public void testGetTarefas() {
        Tarefa tarefa1 = new Tarefa();
        tarefa1.setTitulo("Tarefa 1");
        Tarefa tarefa2 = new Tarefa();
        tarefa2.setTitulo("Tarefa 2");

        when(tarefaRepository.findAll()).thenReturn(Arrays.asList(tarefa1, tarefa2));

        List<Tarefa> tarefas = tarefaController.getTarefas();

        assertEquals(2, tarefas.size());
        assertEquals("Tarefa 1", tarefas.get(0).getTitulo());
        assertEquals("Tarefa 2", tarefas.get(1).getTitulo());
    }
}