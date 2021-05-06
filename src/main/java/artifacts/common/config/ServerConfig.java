package artifacts.common.config;

import artifacts.Artifacts;
import artifacts.common.config.item.*;
import artifacts.common.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class ServerConfig {

    public Set<Item> cosmetics = Collections.emptySet();

    public final Map<Item, ItemConfig> items;

    public final AntidoteVesselConfig antidoteVessel;
    public final BunnyHoppersConfig bunnyHoppers;
    public final CloudInABottleConfig cloudInABottle;
    public final CrossNecklaceConfig crossNecklace;
    public final CrystalHeartConfig crystalHeart;
    public final DiggingClawsConfig diggingClaws;
    public final Map<Item, DrinkingHatConfig> drinkingHats;
    public final Map<Item, EverlastingFoodConfig> everlastingFoods;
    public final FeralClawsConfig feralClaws;
    public final FireGauntletConfig fireGauntlet;
    public final FlamePendantConfig flamePendant;
    public final FlippersConfig flippers;
    public final GoldenHookConfig goldenHook;
    public final LuckyScarfConfig luckyScarf;
    public final ObsidianSkullConfig obsidianSkull;
    public final PanicNecklaceConfig panicNecklace;
    public final PocketPistonConfig pocketPiston;
    public final PowerGloveConfig powerGlove;
    public final RunningShoesConfig runningShoes;
    public final PendantConfig shockPendant;
    public final SuperstitiousHatConfig superstitiousHat;
    public final ThornPendantConfig thornPendant;
    public final UmbrellaConfig umbrella;
    public final UniversalAttractorConfig universalAttractor;
    public final VampiricGloveConfig vampiricGlove;
    public final VillagerHatConfig villagerHat;
    public final WhoopeeCushionConfig whoopeeCushion;

    private final ForgeConfigSpec.ConfigValue<List<String>> cosmeticsValue;

    ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("items");
        cosmeticsValue = builder
                .worldRestart()
                .comment(
                        "List of cosmetic-only items. All items in this list will have their effects disabled",
                        "To blacklist all items, use \"artifacts:*\"",
                        "Note: blacklisting an item while it is equipped may have unintended side effects",
                        "To completely prevent items from appearing, use a data pack"
                )
                .translation(Artifacts.MODID + ".config.server.cosmetics")
                .define("cosmetics", Lists.newArrayList(""));

        items = new HashMap<>();
        drinkingHats = new HashMap<>();
        everlastingFoods = new HashMap<>();

        antidoteVessel = addItemConfig(new AntidoteVesselConfig(builder));
        bunnyHoppers = addItemConfig(new BunnyHoppersConfig(builder));
        cloudInABottle = addItemConfig(new CloudInABottleConfig(builder));
        crossNecklace = addItemConfig(new CrossNecklaceConfig(builder));
        crystalHeart = addItemConfig(new CrystalHeartConfig(builder));
        diggingClaws = addItemConfig(new DiggingClawsConfig(builder));

        drinkingHats.put(ModItems.PLASTIC_DRINKING_HAT.get(), addItemConfig(new DrinkingHatConfig(builder, ModItems.PLASTIC_DRINKING_HAT.get())));
        drinkingHats.put(ModItems.NOVELTY_DRINKING_HAT.get(), addItemConfig(new DrinkingHatConfig(builder, ModItems.NOVELTY_DRINKING_HAT.get())));

        everlastingFoods.put(ModItems.EVERLASTING_BEEF.get(), addItemConfig(new EverlastingFoodConfig(builder, ModItems.EVERLASTING_BEEF.get())));
        everlastingFoods.put(ModItems.ETERNAL_STEAK.get(), addItemConfig(new EverlastingFoodConfig(builder, ModItems.ETERNAL_STEAK.get())));

        feralClaws = addItemConfig(new FeralClawsConfig(builder));
        fireGauntlet = addItemConfig(new FireGauntletConfig(builder));
        flamePendant = addItemConfig(new FlamePendantConfig(builder));
        flippers = addItemConfig(new FlippersConfig(builder));
        goldenHook = addItemConfig(new GoldenHookConfig(builder));
        luckyScarf = addItemConfig(new LuckyScarfConfig(builder));
        obsidianSkull = addItemConfig(new ObsidianSkullConfig(builder));
        panicNecklace = addItemConfig(new PanicNecklaceConfig(builder));
        pocketPiston = addItemConfig(new PocketPistonConfig(builder));
        powerGlove = addItemConfig(new PowerGloveConfig(builder));
        runningShoes = addItemConfig(new RunningShoesConfig(builder));
        shockPendant = addItemConfig(new ShockPendantConfig(builder));
        superstitiousHat = addItemConfig(new SuperstitiousHatConfig(builder));
        thornPendant = addItemConfig(new ThornPendantConfig(builder));
        umbrella = addItemConfig(new UmbrellaConfig(builder));
        universalAttractor = addItemConfig(new UniversalAttractorConfig(builder));
        vampiricGlove = addItemConfig(new VampiricGloveConfig(builder));
        villagerHat = addItemConfig(new VillagerHatConfig(builder));
        whoopeeCushion = addItemConfig(new WhoopeeCushionConfig(builder));

        builder.pop();
    }

    private <T extends ItemConfig> T addItemConfig(T config) {
        items.put(config.getItem(), config);
        return config;
    }

    public void bake() {
        if (cosmeticsValue.get().contains("artifacts:*")) {
            // noinspection ConstantConditions
            cosmetics = ForgeRegistries.ITEMS.getValues()
                    .stream()
                    .filter(item -> item.getRegistryName().getNamespace().equals(Artifacts.MODID))
                    .collect(Collectors.toSet());
        } else {
            cosmetics = cosmeticsValue.get()
                    .stream()
                    .map(ResourceLocation::new)
                    .filter(registryName -> registryName.getNamespace().equals(Artifacts.MODID))
                    .map(ForgeRegistries.ITEMS::getValue)
                    .collect(Collectors.toSet());
        }

        items.forEach((item, config) -> config.bake());
    }

    public boolean isCosmetic(Item item) {
        return cosmetics.contains(item);
    }
}
