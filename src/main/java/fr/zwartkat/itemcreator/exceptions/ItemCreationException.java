package fr.zwartkat.itemcreator.exceptions;

import fr.zwartkat.itemcreator.utils.ChatColorUtil;

public class ItemCreationException extends RuntimeException{

    private String itemId,parameter;

    public ItemCreationException(String itemId, String parameter){
        this.itemId = itemId;
        this.parameter = parameter;
    }

    public String toString(){
        return ChatColorUtil.convert(new String("&c&lAn error occurred while the parameter " + this.parameter + " of " + this.itemId + " has been asked"));
    }
}
