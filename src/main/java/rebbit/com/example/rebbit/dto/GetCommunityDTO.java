package rebbit.com.example.rebbit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetCommunityDTO {

    private final String name;
    private final String description;
    private final LocalDate creationDate;
}
