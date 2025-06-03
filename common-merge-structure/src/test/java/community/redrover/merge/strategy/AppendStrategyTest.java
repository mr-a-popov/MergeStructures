package community.redrover.merge.strategy;

import community.redrover.merge.model.config.AppendStrategyConfig;
import community.redrover.merge.util.FileUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppendStrategyTest {

    @Getter
    @NoArgsConstructor
    @SuppressWarnings("unused")
    static class AppendStrategyTestConfig extends AppendStrategyConfig {

        private String actualResultFile;
        private String expectedResultFile;
        private String errorTargetFile;

        @Override
        public String getResultFile() {
            return actualResultFile;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"yaml", "json"})
    void testAppendStrategyPositiveAndNegativeCases(String format) {
        Path basePath = Paths.get("src/test/resources/append", format);
        Path configPath = basePath.resolve("config." + format);

        AppendStrategyTestConfig config = FileUtils.loadFileToObject(configPath, AppendStrategyTestConfig.class);

        AppendStrategy strategy = new AppendStrategy(config, basePath);
        strategy.execute();

        LinkedHashMap<String, Object> expected = FileUtils.loadFileToMap(basePath.resolve(config.getExpectedResultFile()));
        LinkedHashMap<String, Object> actual = FileUtils.loadFileToMap(basePath.resolve(config.getActualResultFile()));

        assertEquals(expected, actual);

        assertThrows(IllegalStateException.class, () -> {
            LinkedHashMap<String, Object> sourceMap = FileUtils.loadFileToMap(basePath.resolve(config.getSourceFile()));
            LinkedHashMap<String, Object> errorTargetMap = FileUtils.loadFileToMap(basePath.resolve(config.getErrorTargetFile()));

            sourceMap.keySet().retainAll(errorTargetMap.keySet());

            if (!sourceMap.isEmpty()) {
                throw new IllegalStateException("AppendStrategy error: Matching keys found: " + sourceMap.keySet());
            }
        });
    }
}