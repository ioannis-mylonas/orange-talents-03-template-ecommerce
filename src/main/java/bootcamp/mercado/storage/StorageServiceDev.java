package bootcamp.mercado.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceDev implements StorageService {
	public String save(MultipartFile resource) throws IOException {
		return resource.getOriginalFilename();
	}
	
	public List<String> saveAll(List<MultipartFile> resources) throws IOException {
		List<String> res = new ArrayList<>();
		for (MultipartFile resource : resources) {
			res.add(save(resource));
		}
		
		return res;
	}
}
