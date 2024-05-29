package me.lenyz.premierregionstask.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.lenyz.premierregionstask.PremierRegionsTask;
import me.lenyz.premierregionstask.enums.FlagMode;
import me.lenyz.premierregionstask.objects.EditUser;
import me.lenyz.premierregionstask.objects.Flag;
import me.lenyz.premierregionstask.objects.Region;
import me.lenyz.premierregionstask.storage.RegionStorage;
import me.lenyz.premierregionstask.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlagsInventory implements InventoryProvider {

    private EditUser user;
    private Region region;
    private RegionStorage regionStorage;

    public FlagsInventory(EditUser user, Region region, RegionStorage regionStorage){
        this.user = user;
        this.region = region;
        this.regionStorage = regionStorage;
    }

    public static SmartInventory getInventory(EditUser user, Region region, RegionStorage regionStorage){
        return SmartInventory.builder()
                .id("flagsInventory")
                .provider(new FlagsInventory(user, region, regionStorage) {
                })
                .size(5, 9)
                .title("Region Flags")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();

        ArrayList<Flag> flags = new ArrayList<>(PremierRegionsTask.getRegionAPI().getFlagStorage().getFlagsMap().values());
        ClickableItem[] items = new ClickableItem[flags.size()];

        for (int i = 0; i < flags.size(); i++){
            Flag flag = flags.get(i);

            FlagMode mode;
            if(region.getFlags().containsKey(flag.getId())){
                mode = region.getFlags().get(flag.getId());
            }
            else {
                mode = flag.getDefaultValue();
            }

            Material material;
            switch (mode){
                case EVERYONE:
                    material = Material.LIME_STAINED_GLASS_PANE;
                    break;
                case WHITELIST:
                    material = Material.YELLOW_STAINED_GLASS_PANE;
                    break;
                default:
                    material = Material.RED_STAINED_GLASS_PANE;
                    break;
            }

            List<String> lore = new ArrayList<>();
            lore.add("§f");
            lore.add("§7Current Mode: §f" + mode.getName());
            lore.add("§f");
            lore.addAll(flag.getDescription());
            lore.add("§f");
            lore.add("§7Click to change the mode");

            ItemStack itemStack = new ItemBuilder(material)
                    .setDisplayName(flag.getName().replace("&", "§"))
                    .setLore(lore)
                    .build();

            items[i] = ClickableItem.of(itemStack, e -> {
                if(!player.hasPermission("region.flag")){
                    player.sendMessage("§d§lREGION §8» §7You do not have permission to change flags.");
                    return;
                }

                region.addFlag(flag.getId(), getNextMode(mode));
                regionStorage.updateRegion(region);
                FlagsInventory.getInventory(user, region, regionStorage).open(player);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            });
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(10);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 2)
                .blacklist(1, 7)
                .blacklist(1, 8)
                .blacklist(2, 0)
                .blacklist(2, 1));

        if(!pagination.isFirst()){
            contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&cBack").build(), e -> {
                FlagsInventory.getInventory(user, region, regionStorage).open(player, pagination.previous().getPage());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 0);
            }));
        }

        contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&eHome").build(), e -> {
            RegionInventory.getInventory(user, region, regionStorage).open(player);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        if(!pagination.isLast()){
            contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&cNext").build(), e -> {
                FlagsInventory.getInventory(user, region, regionStorage).open(player, pagination.next().getPage());
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 8);
            }));
        }
    }



    @Override
    public void update(Player player, InventoryContents contents) {
    }

    private FlagMode getNextMode(FlagMode mode){
        switch (mode){
            case EVERYONE:
                return FlagMode.WHITELIST;
            case WHITELIST:
                return FlagMode.NONE;
            default:
                return FlagMode.EVERYONE;
        }

    }
}
