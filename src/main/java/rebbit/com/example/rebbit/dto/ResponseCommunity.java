package rebbit.com.example.rebbit.dto;

import lombok.Data;

@Data
public class ResponseCommunity {
    private final Long id;

    private final String name;

    private final Long countOfRows;
    private final Long countOfKarma;


    private final String description;

}
