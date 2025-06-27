package com.tmisehrms.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Program;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Session;
import com.tmisehrms.database.postgreSql.postgreSqlEntity.master.M_Topic;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_ProgramRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_SessionRepository;
import com.tmisehrms.database.postgreSql.postgreSqlRepository.MasterRepos.M_TopicRepository;

import jakarta.persistence.EntityNotFoundException; // Added
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class M_ProgramService {

	@Autowired
	private M_ProgramRepository programRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private M_TopicRepository topicRepository;

	@Autowired
	private M_SessionRepository sessionRepository;

	public M_Program saveProgram(M_Program program) {
		return programRepository.save(program);
	}

	@Transactional
	public M_Program saveProgramWithTopicsAndSessions(Map<String, Object> requestData) {
		M_Program program = new M_Program();
		program.setProgramName((String) requestData.get("programName"));
		program.setFmrcode((String) requestData.get("fmrcode"));
		M_Program savedProgram = programRepository.save(program);

		List<Map<String, Object>> topicsData = objectMapper.convertValue(
				requestData.get("topics"),
				objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

		for (Map<String, Object> topicMap : topicsData) {
			M_Topic topic = objectMapper.convertValue(topicMap, M_Topic.class);
			topic.setProgram(savedProgram);
			M_Topic savedTopic = topicRepository.save(topic);

			Object sessionsObj = topicMap.get("sessions");
			if (sessionsObj != null) {
				List<M_Session> sessions = objectMapper.convertValue(
						sessionsObj,
						objectMapper.getTypeFactory().constructCollectionType(List.class, M_Session.class));
				if (sessions != null && !sessions.isEmpty()) {
					for (M_Session session : sessions) {
						session.setTopic(savedTopic);
						sessionRepository.save(session);
					}
				}
			}
		}
		return savedProgram;
	}

	public Iterable<M_Program> fetchAllProgram() {
		return programRepository.findAll();
	}

	public void deleteProgram(Long id) {
		programRepository.deleteById(id);
	}

	public M_Program updateProgram(Long id, M_Program userDetails) {
		Optional<M_Program> programOptional = programRepository.findById(id);
		if (programOptional.isPresent()) {
			M_Program program = programOptional.get();
			program.setProgramName(userDetails.getProgramName());
			program.setFmrcode(userDetails.getFmrcode());
			return programRepository.save(program);
		} else {
			return null;
		}
	}

	public List<M_Topic> getTopicsByProgramId(Long programId) {
		return topicRepository.findByProgram_ProgramId(programId);
	}

	public Optional<M_Program> findById(Long id) { // Added
		return programRepository.findById(id);
		// .orElseThrow(() -> new EntityNotFoundException("Program not found with ID: "
		// + id)); // Controller handles Optional now
	}
}