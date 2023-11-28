package application.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.model.Quiz;
import application.repository.QuizRepository;

@Controller
@RequestMapping("/quiz")
public class QuizController{
    @Autowired
    private QuizRepository quizRepo;

    @RequestMapping("/list")
    public String list(Model ui){
        ui.addAttribute("quiz", quizRepo.findAll());
        return "quiz/list";
    }

    @RequestMapping("/insert")
    public String insert() {
        return "quiz/insert";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@RequestParam("nome") String nome, Model model){
        Quiz quiz = new Quiz();
        quiz.setNome(nome);
        quizRepo.save(quiz);
        return "redirect:/quiz/list";
    }

    @RequestMapping("/update")
    public String update(
        @RequestParam("id") long id,
        Model ui
    ){
        Optional<Quiz> quiz = quizRepo.findById(id);

        if(quiz.isPresent()){
            ui.addAttribute("quiz", quiz.get());
            return "quiz/update";
        }
        return "redirect:/quiz/list";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
        @RequestParam("id") long id,
        @RequestParam("nome") String nome 
    ) {
        
        Optional<Quiz> quiz = quizRepo.findById(id);

        if(quiz.isPresent()) {
            quiz.get().setNome(nome);

            quizRepo.save(quiz.get());
        }
            
        return "redirect:/quiz/list";
    }

    @RequestMapping("/delete")
    public String delete(
        @RequestParam("id") long id,
        Model ui
    ){
        Optional<Quiz> quiz = quizRepo.findById(id);

        if(quiz.isPresent()){
            ui.addAttribute("quiz", quiz.get());
            return("quiz/delete");
        }

        return "redirect:/quiz/list";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") long id){
        quizRepo.deleteById(id);
        return "redirect:/quiz/list";
    }
}