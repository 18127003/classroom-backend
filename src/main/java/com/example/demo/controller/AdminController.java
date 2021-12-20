package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AbstractServiceEndpoint.ADMIN_PATH)
@RequiredArgsConstructor
public class AdminController extends AbstractServiceEndpoint{

    @PostMapping("account/{id}/lock")
    public ResponseEntity lockAccount(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("account/{id}/unlock")
    public ResponseEntity unlockAccount(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @PutMapping("account/{id}/studentId/map")
    public ResponseEntity mapStudentIdToAccount(@PathVariable Long id, @RequestParam String studentId){
        return ResponseEntity.ok().build();
    }

    @PutMapping("account/{id}/studentId/remove")
    public ResponseEntity removeAccountStudentId(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("class/{id}/remove")
    public ResponseEntity removeClassroom(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
