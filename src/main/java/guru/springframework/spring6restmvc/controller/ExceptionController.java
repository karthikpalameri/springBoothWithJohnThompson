package guru.springframework.spring6restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class ExceptionController {

//    // Exception Handling Technique 2 - Exception Handler on global level
//    @ExceptionHandler(NotFoundException.class)//exception handler for NotFoundException on controller level
//    public ResponseEntity handleNotFounddException() {
//        System.out.println("handleNotFounddException on global level");
//        return ResponseEntity.notFound().build();
//    }
}
