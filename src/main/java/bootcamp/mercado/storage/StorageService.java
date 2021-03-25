package bootcamp.mercado.storage;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	String save(MultipartFile resource);
	List<String> saveAll(List<MultipartFile> resources);
}
