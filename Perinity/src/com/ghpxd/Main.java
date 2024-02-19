package com.ghpxd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.ghpxd.repository.PessoaRepository;
import com.ghpxd.repository.TarefaRepository;
import com.ghpxd.repository.DepartamentoRepository;

@Entity
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nome;
    private String departamento;
    private double totalHoras; // Adicionado campo para armazenar o total de horas

    @OneToMany(mappedBy = "pessoa")
    private List<Tarefa> tarefas;
}

@Entity
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    private String descricao;
    private Date prazo;
    private String departamento;
    private int duracao;
    private boolean finalizado;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
}

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private TarefaRepository tarefaRepository; // Injetado o repositório de tarefas

    @GetMapping
    public List<Pessoa> getPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        for (Pessoa pessoa : pessoas) {
            List<Tarefa> tarefas = tarefaRepository.findByPessoa(pessoa);
            int totalHoras = 0;
            for (Tarefa tarefa : tarefas) {
                totalHoras += tarefa.getDuracao();
            }
            pessoa.setTotalHoras(totalHoras);
        }
        return pessoas;
    }

    @GetMapping("/gastos")
    public List<Pessoa> getPessoasPorNomeEPeriodo(@RequestParam String nome, @RequestParam Date inicio, @RequestParam Date fim) {
        List<Pessoa> pessoas = pessoaRepository.findByNomeAndPeriodo(nome, inicio, fim);
        for (Pessoa pessoa : pessoas) {
            List<Tarefa> tarefas = tarefaRepository.findByPessoaAndPrazoBetween(pessoa, inicio, fim);
            int totalHoras = 0;
            for (Tarefa tarefa : tarefas) {
                totalHoras += tarefa.getDuracao();
            }
            double mediaHoras = (double) totalHoras / tarefas.size();
            pessoa.setMediaHoras(mediaHoras);
        }
        return pessoas;
    }

    @PostMapping
    public Pessoa createPessoa(@RequestBody Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    @PutMapping("/{id}")
    public Pessoa updatePessoa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return pessoaRepository.findById(id).map(p -> {
            p.setNome(pessoa.getNome());
            p.setDepartamento(pessoa.getDepartamento());
            return pessoaRepository.save(p);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @DeleteMapping("/{id}")
    public void deletePessoa(@PathVariable Long id) {
        if (pessoaRepository.existsById(id)) {
            pessoaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada");
        }
    }
}

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaRepository tarefaRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Tarefa> getTarefas() {
        return tarefaRepository.findAll();
    }

    @GetMapping("/pendentes")
    public List<Tarefa> getTarefasPendentes() {
        return tarefaRepository.findTop3ByPessoaIsNullOrderByPrazoAsc();
    }

    @PostMapping
    public Tarefa createTarefa(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    @PutMapping("/alocar/{id}")
    public Tarefa alocarPessoaNaTarefa(@PathVariable Long id, @RequestBody Pessoa pessoa) {
        return tarefaRepository.findById(id).map(t -> {
            Pessoa pessoaExistente = pessoaRepository.findByNomeAndDepartamento(pessoa.getNome(), pessoa.getDepartamento());
            if (pessoaExistente != null) {
                t.setPessoa(pessoaExistente);
                return tarefaRepository.save(t);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com o mesmo departamento não encontrada");
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    @PutMapping("/finalizar/{id}")
    public Tarefa finalizarTarefa(@PathVariable Long id) {
        return tarefaRepository.findById(id).map(t -> {
            t.setFinalizado(true);
            return tarefaRepository.save(t);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    @PutMapping("/{id}")
    public Tarefa updateTarefa(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        return tarefaRepository.findById(id).map(t -> {
            t.setTitulo(tarefa.getTitulo());
            t.setDescricao(tarefa.getDescricao());
            t.setPrazo(tarefa.getPrazo());
            t.setDepartamento(tarefa.getDepartamento());
            t.setDuracao(tarefa.getDuracao());
            t.setFinalizado(tarefa.isFinalizado());
            return tarefaRepository.save(t);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
    }

    @DeleteMapping("/{id}")
    public void deleteTarefa(@PathVariable Long id) {
        if (tarefaRepository.existsById(id)) {
            tarefaRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada");
        }
    }
}