package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.model.Alternativa;
import application.repository.QuestaoRepository;
import application.repository.AlternativaRepository;

@Controller
@RequestMapping("/alternativa")
public class AlternativaController {
    @Autowired
    private AlternativaRepository alternativaRepo;
    private QuestaoRepository questaoRepo;

    @RequestMapping("/list")
    public String list(Model ui) {
        ui.addAttribute("alternativas", alternativaRepo.findAll());
        return "alternativa/list";
    }

    @RequestMapping("/insert")
    public String insert(Model ui){
        ui.addAttribute("questoes",questaoRepo.findAll());
        return "alternativa/insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(
        @RequestParam("questao") long questao,
        @RequestParam("texto") String texto,
        @RequestParam("isCorreta") String isCorreta){
            boolean isCorretaBoolean = Boolean.parseBoolean(isCorreta);
            Alternativa alternativa = new Alternativa();
            alternativa.setTexto(texto);
            alternativa.setCorreta(isCorretaBoolean);
            alternativa.setQuestao(questaoRepo.findById(questao).get());
            
            alternativaRepo.save(alternativa);

            return "redirect:/alternativa/list";
        }
        
        @RequestMapping("/update")
        public String update(
            @RequestParam("id") long id,
            Model ui
        ) {  
            Optional<Alternativa> alternativa = alternativaRepo.findById(id);
            if(alternativa.isPresent()) {
                ui.addAttribute("alternativa", alternativa.get());
                return "alternativa/update";
            }
            return "redirect:/alternativa/list";
        }

        @RequestMapping(value = "/update", method = RequestMethod.POST)
        public String update(
                @RequestParam("id") long id,
                @RequestParam("texto") String texto,
                @RequestParam("isCorreta") String isCorreta
        ) {
            Optional<Alternativa> alternativaOptional = alternativaRepo.findById(id);

            alternativaOptional.ifPresent(alternativa -> {
                boolean isCorretaBoolean = Boolean.parseBoolean(isCorreta);
                alternativa.setTexto(texto);
                alternativa.setCorreta(isCorretaBoolean);

                alternativaRepo.save(alternativa);
            });

            return "redirect:/alternativa/list";
        }

        @RequestMapping("/delete")
        public String delete(
                @RequestParam("id") long id,
                Model ui
        ) {
            Optional<Alternativa> alternativa = alternativaRepo.findById(id);

            if (alternativa.isPresent()) {
                ui.addAttribute("alternativa", alternativa.get());
                return "alternativa/delete";
            }

            return "redirect:/alternativa/list";
        }

        @RequestMapping(value = "/delete", method = RequestMethod.POST)
        public String delete(@RequestParam("id") long id) {
            alternativaRepo.deleteById(id);
            return "redirect:/alternativa/list";
        }
}