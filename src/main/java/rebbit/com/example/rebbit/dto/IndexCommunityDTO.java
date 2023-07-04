package rebbit.com.example.rebbit.dto;

import lombok.Data;

@Data
public class IndexCommunityDTO {
    private final Long id;

    private final String name;

    private final String description;

    private final String fileName;
    private final Long count;
    private final Long karma;
}
