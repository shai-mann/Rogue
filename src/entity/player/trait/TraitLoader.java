package entity.player.trait;

import entity.player.trait.reward.Reward;
import entity.player.trait.reward.StatReward;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TraitLoader {

    private static String FILE_PATH;
    private static List<Trait> traits;

    static {
        try {
            FILE_PATH = new File(
                    Objects.requireNonNull(
                            TraitLoader.class.getClassLoader().getResource("./data/traits")
                    ).toURI()
            ).getPath();
        } catch (URISyntaxException e) {
            new Exception("Failed to locate trait files").printStackTrace();
            e.printStackTrace();
        }
    }

    public static List<Trait> traits() {
        return traits;
    }

    public static void loadTraits() {
        try {
            traits = load(Objects.requireNonNull(new File(FILE_PATH).listFiles()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trait files: " + FILE_PATH + "\n" + e.getMessage());
        }
    }

    public static List<Trait> load(File[] files) throws IOException {
        return Arrays.stream(files).map(TraitLoader::load).toList();
    }

    private static Trait load(File file) {
        JSONObject json;
        try {
            json = new JSONObject(Files.readString(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String name = json.getString("name");
        List<List<Reward>> levels = new ArrayList<>();

        JSONArray jsonLevels = json.getJSONArray("levels");
        for (int i = 0; i < jsonLevels.length(); i++) {
            levels.add(loadRewards(jsonLevels.getJSONObject(i)));
        }

        return new TraitImpl(name, levels);
    }

    private static List<Reward> loadRewards(JSONObject json) {
        List<Reward> out = new ArrayList<>();

        if (json.has("stat")) {
            out.addAll(loadStatRewards(json.getJSONArray("stat")));
        }

        if (json.has("unlock")) {
            out.addAll(loadUnlockRewards(json.getJSONArray("unlock")));
        }

        return out;
    }

    private static List<Reward> loadStatRewards(JSONArray data) {
        List<Reward> out = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            out.add(loadStatReward(data.getJSONObject(i)));
        }

        return out;
    }

    // todo: move to hardcoded constants instead of randomly placed strings
    private static Reward loadStatReward(JSONObject data) {
        return new StatReward(
                data.getString("type"),
                data.getString("trait"),
                data.getNumber("delta")
        );
    }

    private static List<Reward> loadUnlockRewards(JSONArray data) {
        return new ArrayList<>(); // todo: unlock rewards
    }

}
