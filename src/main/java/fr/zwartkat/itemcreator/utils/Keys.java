package fr.zwartkat.itemcreator.utils;

import fr.zwartkat.itemcreator.ItemCreator;
import org.bukkit.NamespacedKey;

public class Keys {

    public static final NamespacedKey IDKEY = new NamespacedKey(ItemCreator.getInstance().getName().toLowerCase(),"item_id");
    public static final NamespacedKey DURAKEY = new NamespacedKey(ItemCreator.getInstance().getName().toLowerCase(),"durability");
    public static final NamespacedKey UNIQUEID = new NamespacedKey(ItemCreator.getInstance().getName().toLowerCase(),"unique_id");

}
