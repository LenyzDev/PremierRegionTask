package me.lenyz.premierregionstask.storage;

import me.lenyz.premierregionstask.enums.EditMode;
import me.lenyz.premierregionstask.objects.EditUser;

import java.util.HashMap;
import java.util.UUID;

public class UserStorage {

    private HashMap<UUID, EditUser> usersMap;

    public UserStorage() {
        this.usersMap = new HashMap<>();
    }

    public void addUser(UUID id, EditUser user) {
        this.usersMap.put(id, user);
    }

    public void removeUser(UUID id) {
        this.usersMap.remove(id);
    }

    public boolean containsUser(UUID id) {
        return this.usersMap.containsKey(id);
    }

    public EditUser getUser(UUID id) {
        if(!this.containsUser(id)){
            usersMap.put(id, new EditUser(id, EditMode.NONE));
        }
        return this.usersMap.get(id);
    }

    public HashMap<UUID, EditUser> getUsersMap() {
        return usersMap;
    }
}
