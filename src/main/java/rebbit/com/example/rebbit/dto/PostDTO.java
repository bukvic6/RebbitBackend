package rebbit.com.example.rebbit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostDTO {
    private final String title;
    private final String text;
    private final Integer karma;

    private final Long communityDTO;

}
