package rebbit.com.example.rebbit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostResponse {
    private final Long id;
    private final String title;
    private final String text;
    private final LocalDate creationDate;
    private final String username;
    private final Integer karma;

    private final String comunityName;
}
