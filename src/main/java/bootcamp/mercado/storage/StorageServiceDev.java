package bootcamp.mercado.storage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceDev implements StorageService {
	public String save(MultipartFile resource) {
		return resource.getOriginalFilename();
	}
	
	public List<String> saveAll(List<MultipartFile> resources) {
		List<String> res = new ArrayList<>();
		for (MultipartFile resource : resources) {
			res.add(save(resource));
		}
		
		return res;
	}
}
