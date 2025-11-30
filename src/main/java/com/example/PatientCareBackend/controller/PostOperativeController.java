package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.PostOperativeRequest;
import com.example.PatientCareBackend.dto.response.PostOperativeResponse;
import com.example.PatientCareBackend.model.PostOperative;
import com.example.PatientCareBackend.service.PostOperativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postoperative")
@RequiredArgsConstructor
public class PostOperativeController {

    private final PostOperativeService postOperativeService;

    @PostMapping
    public ResponseEntity<PostOperativeResponse> recordFollowup(@Valid @RequestBody PostOperativeRequest followupRequest) {
        PostOperativeResponse followup = postOperativeService.recordFollowup(followupRequest);
        return new ResponseEntity<>(followup, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PostOperativeResponse>> getPatientFollowups(@PathVariable Long patientId) {
        List<PostOperativeResponse> followups = postOperativeService.getPatientFollowups(patientId);
        return ResponseEntity.ok(followups);
    }

    @GetMapping("/surgery/{surgeryId}")
    public ResponseEntity<List<PostOperativeResponse>> getSurgeryFollowups(@PathVariable Long surgeryId) {
        List<PostOperativeResponse> followups = postOperativeService.getSurgeryFollowups(surgeryId);
        return ResponseEntity.ok(followups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostOperativeResponse> getFollowupById(@PathVariable Long id) {
        PostOperativeResponse followup = postOperativeService.getFollowupById(id);
        return ResponseEntity.ok(followup);
    }

    @GetMapping("/type/{followupType}")
    public ResponseEntity<List<PostOperativeResponse>> getFollowupsByType(@PathVariable PostOperative.FollowupType followupType) {
        List<PostOperativeResponse> followups = postOperativeService.getFollowupsByType(followupType);
        return ResponseEntity.ok(followups);
    }

    @GetMapping("/non-adherent")
    public ResponseEntity<List<PostOperativeResponse>> getNonAdherentPatients() {
        List<PostOperativeResponse> followups = postOperativeService.getNonAdherentPatients();
        return ResponseEntity.ok(followups);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<PostOperativeResponse>> getOverdueFollowups() {
        List<PostOperativeResponse> followups = postOperativeService.getOverdueFollowups();
        return ResponseEntity.ok(followups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostOperativeResponse> updateFollowup(
            @PathVariable Long id,
            @Valid @RequestBody PostOperativeRequest followupRequest) {
        PostOperativeResponse followup = postOperativeService.updateFollowup(id, followupRequest);
        return ResponseEntity.ok(followup);
    }
}