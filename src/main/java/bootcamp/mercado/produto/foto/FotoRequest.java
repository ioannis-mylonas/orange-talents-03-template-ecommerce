package bootcamp.mercado.produto.foto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class FotoRequest {
	@NotNull @Size(min = 1)
	private List<MultipartFile> fotos;
	
	public FotoRequest(List<MultipartFile> fotos) {
		this.fotos = fotos;
	}
	
	public List<MultipartFile> getFotos() {
		return fotos;
	}
	
	public static List<Foto> converte(List<String> uriList) {
		return uriList.stream().map(Foto::new).collect(Collectors.toList());
	}
}
