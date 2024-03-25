package edu.iu.c322.midterm.controllers;

import edu.iu.c322.midterm.model.Question;
import edu.iu.c322.midterm.model.Quiz;
import edu.iu.c322.midterm.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
public class QuizController {

        private FileRepository fileRepository;

        public QuizController(FileRepository fileRepository) {
            this.fileRepository = fileRepository;
        }

        @PostMapping
        public int add(@RequestBody Quiz quiz) {
            try {
                return fileRepository.addQuiz(quiz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @GetMapping
        public List<Quiz> findAll() {
            try {
                List<Quiz> quizzes = fileRepository.findAllQuizzes();
                for(Quiz quiz : quizzes){
                    quiz.setQuestions(fileRepository.find(quiz.getQuestionIds()));
                }
                return quizzes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> get(@PathVariable Integer id) throws IOException {
        Quiz quiz = fileRepository.getQuiz(id);
        if (quiz == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        quiz.setQuestions(fileRepository.find(quiz.getQuestionIds()));

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@PathVariable int id, @RequestBody Quiz quiz) {
        try {
            if(fileRepository.getQuiz(id) != null){
                fileRepository.updateQuiz(id, quiz.getQuestionIds(), quiz.getTitle());
                return ResponseEntity.ok("updated");
            }else{
                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
