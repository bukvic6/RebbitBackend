package rebbit.com.example.rebbit.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class PostDTO {
    private final String title;
    private final String text;
    private final Long communityDTO;
    private final MultipartFile pdf;

}
