package com.ghpxd.repository;

import com.ghpxd.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Departamento findByNome(String nome);
}

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private PessoaRepository pessoaRepository;
    @Autowired
    private TarefaRepository tarefaRepository;

    @GetMapping
    public List<Map<String, Object>> getDepartamentos() {
        List<Map<String, Object>> departamentos = new ArrayList<>();
        List<Departamento> todosDepartamentos = departamentoRepository.findAll();

        for (Departamento departamento : todosDepartamentos) {
            Map<String, Object> depto = new HashMap<>();
            depto.put("nome", departamento.getNome());
            depto.put("quantidadePessoas", pessoaRepository.countByDepartamento(departamento));
            depto.put("quantidadeTarefas", tarefaRepository.countByDepartamento(departamento));
            departamentos.add(depto);
        }

        return departamentos;
    }
}