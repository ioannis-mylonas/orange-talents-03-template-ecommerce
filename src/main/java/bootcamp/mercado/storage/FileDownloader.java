package bootcamp.mercado.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileDownloader {
	String save(MultipartFile resource);
	List<String> saveAll(List<MultipartFile> resources);
}
