package me.lenyz.premierregionstask.inventories;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.lenyz.premierregionstask.enums.EditMode;
import me.lenyz.premierregionstask.objects.Cuboid;
import me.lenyz.premierregionstask.objects.EditUser;
import me.lenyz.premierregionstask.objects.Region;
import me.lenyz.premierregionstask.storage.RegionStorage;
import me.lenyz.premierregionstask.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RegionInventory implements InventoryProvider {

    private EditUser user;
    private Region region;
    private RegionStorage regionStorage;

    public RegionInventory(EditUser user, Region region, RegionStorage regionStorage){
        this.user = user;
        this.region = region;
        this.regionStorage = regionStorage;
    }

    public static SmartInventory getInventory(EditUser user, Region region, RegionStorage regionStorage){
        return SmartInventory.builder()
                .id("regionInventory")
                .provider(new RegionInventory(user, region, regionStorage) {
                })
                .size(5, 9)
                .title("Region")
                .build();
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        ItemStack rename = new ItemBuilder(Material.PAPER)
                .setDisplayName("§dRename")
                .setLore(List.of(
                        "",
                        "§fCurrent Name: §d" + region.getName(),
                        "",
                        "§dClick to Change."
                ))
                .build();

        ItemStack addWhitelist = new ItemBuilder(Material.LIME_DYE)
                .setDisplayName("§aAdd Whitelist")
                .setLore(List.of(
                        "",
                        "§aClick to Add."
                ))
                .build();

        ItemStack removeWhitelist = new ItemBuilder(Material.RED_DYE)
                .setDisplayName("§cRemove Whitelist")
                .setLore(List.of(
                        "",
                        "§cClick to Remove."
                ))
                .build();

        ItemStack redefineLocation = new ItemBuilder(Material.COMPASS)
                .setDisplayName("§bRedefine Location")
                .setLore(List.of(
                        "",
                        "§bClick to Redefine."
                ))
                .build();

        ItemStack flags = new ItemBuilder(Material.YELLOW_BANNER)
                .setDisplayName("§eFlags")
                .setLore(List.of(
                        "",
                        "§eClick to View."
                ))
                .build();

        contents.set(1, 2, ClickableItem.of(rename, e -> {
            if(!player.hasPermission("region.create")){
                player.sendMessage("§d§lREGION §8» §7You do not have permission to rename the region.");
                return;
            }

            player.sendMessage("§d§lREGION §8» §7Please enter the new name for the region.");
            user.setEditMode(EditMode.NAME);
            user.setRegionId(region.getName());
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        contents.set(1, 3, ClickableItem.of(addWhitelist, e -> {
            if(!player.hasPermission("region.add")){
                player.sendMessage("§d§lREGION §8» §7You do not have permission to add players to whitelist.");
                return;
            }

            player.sendMessage("§d§lREGION §8» §7Please enter the name of the player you want to add to the whitelist.");
            user.setEditMode(EditMode.ADD_WHITELIST);
            user.setRegionId(region.getName());
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        contents.set(1, 4, ClickableItem.of(removeWhitelist, e -> {
            if(!player.hasPermission("region.remove")){
                player.sendMessage("§d§lREGION §8» §7You do not have permission to remove players from whitelist.");
                return;
            }

            player.sendMessage("§d§lREGION §8» §7Please enter the name of the player you want to remove from the whitelist.");
            user.setEditMode(EditMode.REMOVE_WHITELIST);
            user.setRegionId(region.getName());
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        contents.set(1, 5, ClickableItem.of(redefineLocation, e -> {
            if(!player.hasPermission("region.create")){
                player.sendMessage("§d§lREGION §8» §7You do not have permission to redefine the location.");
                return;
            }

            if(user.getFirstPosition() == null || user.getSecondPosition() == null){
                player.sendMessage("§d§lREGION §8» §7Please select the region first.");
                return;
            }
            region.setCuboid(new Cuboid(player.getWorld().getName(), user.getFirstPosition(), user.getSecondPosition()));
            player.sendMessage("§d§lREGION §8» §7Region location has been redefined.");
            regionStorage.updateRegion(region);
            user.setFirstPosition(null);
            user.setSecondPosition(null);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        contents.set(1, 6, ClickableItem.of(flags, e -> {
            if(!player.hasPermission("region.flag")){
                player.sendMessage("§d§lREGION §8» §7You do not have permission to change flags.");
                return;
            }

            FlagsInventory.getInventory(user, region, regionStorage).open(player);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));

        contents.set(4, 4, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&eHome").build(), e -> {
            player.performCommand("region");
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 4, 4);
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
    }
}
