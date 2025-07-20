package com.ehrms.tmis.Users.ResourcePerson.Servcie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_Resources;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo.T_ResouresRepository;

@Service
public class ResourcePersonService {

    @Autowired
    private T_ResouresRepository tResourcesRepository;

public List<T_Resources> saveResources(String empCd, MultipartFile[] files) throws IOException {
  List<T_Resources> savedResources = new ArrayList<>();

  for (MultipartFile file : files) {
    T_Resources resource = T_Resources.builder()
        .empCd(empCd)
        .resource(file.getBytes())
        .fileName(file.getOriginalFilename())
        .fileType(file.getContentType())
        .build();

    savedResources.add(tResourcesRepository.save(resource));
  }

  return savedResources;
}

public List<T_Resources> getResourcesByEmpCd(String empCd) {
  return tResourcesRepository.findAllByEmpCd(empCd);
}

public T_Resources getResourceById(Long id) {
  return tResourcesRepository.findById(id).orElse(null);
}


}
