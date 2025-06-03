package community.redrover.merge;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {

        ObjectMapper objectMapper = YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build();

        Map<String, Object> fileMap = objectMapper.readValue(
                new File("D:\\Work\\Projects\\npgw-api-specification\\api-merchant.yaml"), new TypeReference<HashMap<String, Object>>() {});


        System.out.println( fileMap );
    }
}