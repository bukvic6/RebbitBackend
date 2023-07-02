package rebbit.com.example.rebbit.model;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;

@Document(indexName = "rebbit_community")
@Setting(settingPath = "analyzers/serbianAnalyzer.json")
@Data
public class IndexCommunity {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String fileName;

    @Field(type = FieldType.Text)
    private String pdfContent;

    public IndexCommunity(Community community) {
        this.id = community.getId();
        this.description = community.getDescription();
        this.name = community.getName();
    }

    public IndexCommunity(Community community, String pdfContent, String fileName) {
        this.id = community.getId();
        this.description = community.getDescription();
        this.name = community.getName();
        this.pdfContent = pdfContent;
        this.fileName = fileName;
    }


}
