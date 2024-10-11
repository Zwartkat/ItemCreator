package fr.zwartkat.itemcreator.items;

import fr.zwartkat.itemcreator.items.effects.GivePotionEffect;

import java.util.HashMap;

public class ItemEffectLoader {

    private HashMap<String, Class> loadedEffects;

    public ItemEffectLoader(){
        this.loadedEffects = new HashMap<>();

        this.registerEffect("give_effect", GivePotionEffect.class);
    }

    private void registerEffect(String effectId, Class effectClass){
        this.loadedEffects.put(effectId,effectClass);
    }

    public Class getEffectClass(String effectId){
        if(this.isEffect(effectId)){
            return this.loadedEffects.get(effectId);
        }
        else {
            return null;
        }

    }

    public Boolean isEffect(String effectId){
        if(this.loadedEffects.containsKey(effectId)){
            return true;
        }
        else {
            return false;
        }
    }

    public HashMap<String,Class> getLoadedEffects(){
        return this.loadedEffects;
    }
}
