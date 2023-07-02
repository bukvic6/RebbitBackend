package rebbit.com.example.rebbit.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CommunityDTO {
    private final String name;
    private final String description;
    private final MultipartFile pdf;
}
