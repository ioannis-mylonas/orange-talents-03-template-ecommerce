package bootcamp.mercado.storage;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	String save(MultipartFile resource) throws IOException;
	List<String> saveAll(List<MultipartFile> resources) throws IOException;
}
