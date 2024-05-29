package me.lenyz.premierregionstask.commands;

import me.lenyz.premierregionstask.enums.FlagMode;
import me.lenyz.premierregionstask.inventories.RegionInventory;
import me.lenyz.premierregionstask.inventories.RegionListInventory;
import me.lenyz.premierregionstask.objects.*;
import me.lenyz.premierregionstask.storage.FlagStorage;
import me.lenyz.premierregionstask.storage.RegionStorage;
import me.lenyz.premierregionstask.storage.UserStorage;
import me.lenyz.premierregionstask.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RegionCommand implements CommandExecutor {

    private UserStorage userStorage;
    private RegionStorage regionStorage;
    private FlagStorage flagStorage;

    public RegionCommand(UserStorage userStorage, RegionStorage regionStorage, FlagStorage flagStorage) {
        this.userStorage = userStorage;
        this.regionStorage = regionStorage;
        this.flagStorage = flagStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            if (!(sender instanceof Player)) {
                sender.sendMessage("§d§lREGION §8» §cYou must be a player to execute this command!");
                return false;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("region.menu")) {
                player.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                return false;
            }
            EditUser user = userStorage.getUser(player.getUniqueId());
            RegionListInventory.getInventory(user, regionStorage).open(player);
            return false;
        }else{
            switch (args[0]){
                case("create"):{
                    if(args.length != 2){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region create <name>");
                        return false;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§d§lREGION §8» §cYou must be a player to execute this command!");
                        return false;
                    }
                    Player player = (Player) sender;
                    if(!player.hasPermission("region.create")) {
                        player.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    EditUser user = userStorage.getUser(player.getUniqueId());
                    String regionName = args[1];
                    if(regionStorage.isRegionExists(regionName)){
                        player.sendMessage("§d§lREGION §8» §cThis region already exists.");
                        return false;
                    }

                    if(user.getFirstPosition() == null || user.getSecondPosition() == null){
                        player.sendMessage("§d§lREGION §8» §cYou must set the first and second position.");
                        return false;
                    }

                    Cuboid cuboid = new Cuboid(player.getWorld().getName(), user.getFirstPosition(), user.getSecondPosition());
                    regionStorage.createRegion(regionName, new Region(
                            regionName,
                            new ArrayList<>(),
                            new HashMap<>(),
                            cuboid
                    ));
                    userStorage.removeUser(player.getUniqueId());
                    player.sendMessage("§d§lREGION §8» §7Region §f"+regionName+"§7 created.");
                    return true;
                }
                case("delete"):{
                    if(args.length != 2){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region delete <name>");
                        return false;
                    }

                    if(!sender.hasPermission("region.create")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    String regionName = args[1];
                    if(!regionStorage.isRegionExists(regionName)){
                        sender.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }

                    regionStorage.removeRegion(regionName);
                    sender.sendMessage("§d§lREGION §8» §7Region §f"+regionName+"§7 deleted.");
                    regionStorage.deleteRegion(regionName);
                    return true;
                }
                case("wand"):{
                    if(args.length != 1){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region wand");
                        return false;
                    }

                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§d§lREGION §8» §cYou must be a player to execute this command!");
                        return false;
                    }
                    Player player = (Player) sender;
                    if(!player.hasPermission("region.create")) {
                        player.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }
                    player.getInventory().addItem(new ItemBuilder(Material.STICK).setDisplayName("§d§lREGION WAND").build());
                    player.sendMessage("§d§lREGION §8» §7You have received the region wand.");
                    return true;
                }
                case("add"):{
                    if(args.length != 3){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region add <name> <player>");
                        return false;
                    }

                    if(!sender.hasPermission("region.add")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    String regionName = args[1];
                    if(!regionStorage.isRegionExists(regionName)){
                        sender.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }

                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[2]);
                    if(offlinePlayer == null){
                        sender.sendMessage("§d§lREGION §8» §cThis player doesn't exist.");
                        return false;
                    }

                    Region region = regionStorage.getRegion(regionName);
                    if(region.getMembers().contains(offlinePlayer.getUniqueId())){
                        sender.sendMessage("§d§lREGION §8» §cThis player is already on the whitelist.");
                        return false;
                    }

                    region.addMembers(offlinePlayer.getUniqueId());
                    sender.sendMessage("§d§lREGION §8» §7Player §f"+offlinePlayer.getName()+"§7 added to the region §f"+regionName+"§7.");
                    regionStorage.updateRegion(region);
                    return true;
                }
                case("remove"):{
                    if(args.length != 3){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region remove <name> <player>");
                        return false;
                    }

                    if(!sender.hasPermission("region.remove")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    String regionName = args[1];
                    if(!regionStorage.isRegionExists(regionName)){
                        sender.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }

                    OfflinePlayer offlinePlayer = sender.getServer().getOfflinePlayer(args[2]);
                    if(offlinePlayer == null){
                        sender.sendMessage("§d§lREGION §8» §cThis player doesn't exist.");
                        return false;
                    }

                    Region region = regionStorage.getRegion(regionName);
                    if(!region.getMembers().contains(offlinePlayer.getUniqueId())){
                        sender.sendMessage("§d§lREGION §8» §cThis player is not on the whitelist.");
                        return false;
                    }

                    region.removeMembers(offlinePlayer.getUniqueId());
                    sender.sendMessage("§d§lREGION §8» §7Player §f"+offlinePlayer.getName()+"§7 removed from the region §f"+regionName+"§7.");
                    regionStorage.updateRegion(region);
                    return true;
                }
                case("whitelist"):{
                    if(args.length != 2){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region whitelist <name>");
                        return false;
                    }

                    if(!sender.hasPermission("region.whitelist")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    String regionName = args[1];
                    if(!regionStorage.isRegionExists(regionName)){
                        sender.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }

                    Region region = regionStorage.getRegion(regionName);
                    sender.sendMessage("§d§lREGION §8» §7Whitelist of the region §f"+regionName+"§7:");
                    for(UUID memberUUID : region.getMembers()){
                        OfflinePlayer member = sender.getServer().getOfflinePlayer(memberUUID);
                        if(member == null) {
                            sender.sendMessage("§f- §cUnknown Player §7("+memberUUID+")");
                            continue;
                        }
                        sender.sendMessage("§f- "+member.getName());
                    }
                    return true;
                }
                case("flag"):{
                    if(args.length != 4){
                        sender.sendMessage("§d§lREGION §8» §cUsage: /region flag <name> <flag> <state>");
                        return false;
                    }

                    if(!sender.hasPermission("region.flag")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }

                    String regionName = args[1];
                    if(!regionStorage.isRegionExists(regionName)){
                        sender.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }

                    Region region = regionStorage.getRegion(regionName);
                    Flag flag = flagStorage.getFlag(args[2]);
                    if(flag == null){
                        sender.sendMessage("§d§lREGION §8» §cThis flag doesn't exist. Registered flags:");
                        for(Flag registeredFlag : flagStorage.getFlagsMap().values()){
                            sender.sendMessage("§f- "+registeredFlag.getId()+" §7(Default Value: "+registeredFlag.getDefaultValue().getName()+")");
                        }
                        return false;
                    }

                    String stateString = args[3];
                    if(!stateString.equalsIgnoreCase("everyone") && !stateString.equalsIgnoreCase("whitelist") && !stateString.equalsIgnoreCase("none")){
                        sender.sendMessage("§d§lREGION §8» §cInvalid state. §7(Use Everyone, Whitelist or None)");
                        return false;
                    }

                    FlagMode state = FlagMode.valueOf(stateString.toUpperCase());
                    region.addFlag(flag.getName(), state);
                    sender.sendMessage("§d§lREGION §8» §7Flag §f"+flag.getName().replace("&", "§")+"§7 set to §f"+stateString+"§7 in region §f"+regionName+"§7.");
                    regionStorage.updateRegion(region);
                    return true;
                }
                default:{
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§d§lREGION §8» §cYou must be a player to execute this command!");
                        return false;
                    }
                    if(!sender.hasPermission("region.menu")) {
                        sender.sendMessage("§d§lREGION §8» §cYou don't have permission to use this command.");
                        return false;
                    }
                    Player player = (Player) sender;
                    String regionName = args[0];
                    if(!regionStorage.isRegionExists(regionName)) {
                        player.sendMessage("§d§lREGION §8» §cThis region doesn't exist.");
                        return false;
                    }
                    EditUser user = userStorage.getUser(player.getUniqueId());
                    Region region = regionStorage.getRegion(regionName);
                    RegionInventory.getInventory(user, region, regionStorage).open(player);
                    return true;
                }
            }
        }
    }
}
