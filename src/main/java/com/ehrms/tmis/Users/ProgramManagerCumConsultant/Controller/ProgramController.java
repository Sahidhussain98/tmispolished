package com.ehrms.tmis.Users.ProgramManagerCumConsultant.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehrms.tmis.Users.ProgramManagerCumConsultant.Service.M_ProgramService;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Program;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Topic;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {

	private final M_ProgramService programService;

	public ProgramController(M_ProgramService programService) {
		this.programService = programService;
	}

	@PostMapping("/save")
	public M_Program saveProgram(@RequestBody M_Program program) {
		return programService.saveProgram(program);
	}

	@GetMapping("/getall")
	public ResponseEntity<Iterable<M_Program>> getAllPrograms() {
		Iterable<M_Program> programs = programService.fetchAllProgram();
		return new ResponseEntity<>(programs, HttpStatus.OK);
	}

	@PostMapping("/saveProgramWithTopicsAndSessions")
	public M_Program saveProgramWithTopicsAndSessions(@RequestBody Map<String, Object> requestData) {
		return programService.saveProgramWithTopicsAndSessions(requestData);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteProgram(@RequestBody Map<String, String> requestBody) {
		try {
			Long id = Long.valueOf(requestBody.get("id"));
			programService.deleteProgram(id);
			return ResponseEntity.noContent().build();
		} catch (NumberFormatException | NullPointerException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/update")
	public ResponseEntity<M_Program> updateProgram(@RequestBody M_Program programDetails) {
		M_Program updatedProgram = programService.updateProgram(programDetails.getProgramId(), programDetails);
		return updatedProgram != null ? new ResponseEntity<>(updatedProgram, HttpStatus.OK)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/topics/by-program/{programId}")
	public ResponseEntity<List<M_Topic>> getTopicsByProgramId(@PathVariable Long programId) {
		if (programId == null) {
			return ResponseEntity.badRequest().body(Collections.emptyList());
		}
		List<M_Topic> topics = programService.getTopicsByProgramId(programId);
    		return ResponseEntity.ok(topics);
	}
}